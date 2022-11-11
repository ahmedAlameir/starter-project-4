package com.udacity.project4.locationreminders.reminderslist

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.FirebaseApp
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(maxSdk = Build.VERSION_CODES.P)

class RemindersListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeRepo: FakeDataSource
    private lateinit var remindersListViewModel: RemindersListViewModel

    @Before
    fun createRepository() {
        stopKoin()
        FirebaseApp.initializeApp(getApplicationContext())
        fakeRepo = FakeDataSource()
        remindersListViewModel = RemindersListViewModel(
            getApplicationContext(),
            fakeRepo
        )
    }

    @Test
    fun check_loading() = runBlockingTest {
        fakeRepo.deleteAllReminders()
        val reminder = ReminderDTO("Title", "Description", "Location", 19.0, 20.2)
        fakeRepo.saveReminder(reminder)

        mainCoroutineRule.pauseDispatcher()

        remindersListViewModel.loadReminders()
        MatcherAssert.assertThat(
            remindersListViewModel.showLoading.getOrAwaitValue(),`is`(true)
        )

        mainCoroutineRule.resumeDispatcher()
        MatcherAssert.assertThat(
            remindersListViewModel.showLoading.getOrAwaitValue(), `is`(false)
        )
    }
    @Test
    fun shouldReturnError() = runBlockingTest {
        fakeRepo.setReturnsError(true)
        remindersListViewModel.loadReminders()

        MatcherAssert.assertThat(
            remindersListViewModel.showSnackBar.getOrAwaitValue(),`is`("data couldn't be retrieved")
        )
    }


    //TODO: provide testing to the RemindersListViewModel and its live data objects

}