package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource (var reminders: MutableList<ReminderDTO> = mutableListOf()) : ReminderDataSource {

//    TODO: Create a fake data source to act as a double to the real data source

    private  var returnsError = false

    fun setReturnsError(value: Boolean) {
        returnsError = value
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return if (returnsError){
            Result.Error("data couldn't be retrieved", 404)
        }else{
            Result.Success(ArrayList(reminders))
        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        return if (returnsError) {
            Result.Error("data couldn't be retrieved", 404)
        } else {
            val reminder = reminders.find { it.id == id }
            if (reminder != null) {
                Result.Success(reminder)
            } else {
                Result.Error("id not found", 404)

            }
        }
    }

    override suspend fun deleteAllReminders() {
        reminders.clear()
    }


}