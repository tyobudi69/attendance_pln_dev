package com.example.absensi_pln_dev.viewmodel

import androidx.lifecycle.*
import com.example.absensi_pln_dev.data.model.Attendance
import com.example.absensi_pln_dev.repo.AttendanceRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class AttendanceViewModel(private val repository: AttendanceRepository) : ViewModel() {

    // Function to handle user sign-in
    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            val user = repository.getUser(email)
            if (user != null && user.password == password) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    // Function to register new users
    fun insertUser(user: com.example.absensi_pln_dev.data.model.User) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }

    // Function to record "Time In"
    fun recordTimeIn(email: String, latitude: Double?, longitude: Double?, timeInImageUri: String?) {
        viewModelScope.launch {
            val timeInAttendance = Attendance(
                email = email,
                timeIn = System.currentTimeMillis(),
                timeInLatitude = latitude,
                timeInLongitude = longitude,
                timeInImageUri = timeInImageUri
            )
            repository.insertAttendance(timeInAttendance)
        }
    }

    // Function to record "Time Out"
    fun recordTimeOut(email: String, latitude: Double?, longitude: Double?, timeOutImageUri: String?) {
        viewModelScope.launch {
            val attendance = repository.getLatestAttendanceForUser(email)
            attendance?.let {
                it.timeOut = System.currentTimeMillis()
                it.timeOutLatitude = latitude
                it.timeOutLongitude = longitude
                it.timeOutImageUri = timeOutImageUri
                repository.updateAttendance(it)
            }
        }
    }

    // Get attendance history for a user
    fun getAttendanceHistory(email: String): Flow<List<Attendance>> {
        return repository.getAttendanceHistory(email)
    }

    // ViewModel Factory
    class Factory(private val repository: AttendanceRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AttendanceViewModel(repository) as T
        }
    }
}
