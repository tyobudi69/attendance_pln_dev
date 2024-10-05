package com.example.absensi_pln_dev.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.absensi_pln_dev.data.dao.AttendanceDao
import com.example.absensi_pln_dev.data.dao.UserDao
import com.example.absensi_pln_dev.data.model.Attendance
import com.example.absensi_pln_dev.data.model.User

@Database(entities = [User::class, Attendance::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun attendanceDao(): AttendanceDao
}
