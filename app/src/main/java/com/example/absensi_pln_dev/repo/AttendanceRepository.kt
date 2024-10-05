package com.example.absensi_pln_dev.repo

import com.example.absensi_pln_dev.data.dao.AttendanceDao
import com.example.absensi_pln_dev.data.dao.UserDao
import com.example.absensi_pln_dev.data.model.Attendance
import com.example.absensi_pln_dev.data.model.User
import kotlinx.coroutines.flow.Flow

import androidx.room.*


class AttendanceRepository(private val userDao: UserDao, private val attendanceDao: AttendanceDao) {

    // Insert a new user
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    // Retrieve a user by email
    suspend fun getUser(email: String): User? {
        return userDao.getUser(email)
    }

    // Insert a new attendance record
    suspend fun insertAttendance(attendance: Attendance) {
        attendanceDao.insertAttendance(attendance)
    }

    // Update an existing attendance record
    suspend fun updateAttendance(attendance: Attendance) {
        attendanceDao.updateAttendance(attendance)
    }

    // Get the latest attendance record for a specific user
    suspend fun getLatestAttendanceForUser(email: String): Attendance? {
        return attendanceDao.getLatestAttendanceForUser(email)
    }

    // Get the attendance history for a user
    fun getAttendanceHistory(email: String): Flow<List<Attendance>> {
        return attendanceDao.getAttendanceHistory(email)
    }
}