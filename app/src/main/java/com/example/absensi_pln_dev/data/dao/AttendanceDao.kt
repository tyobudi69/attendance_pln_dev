package com.example.absensi_pln_dev.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.absensi_pln_dev.data.model.Attendance
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: Attendance)

    @Update
    suspend fun updateAttendance(attendance: Attendance)

    @Query("SELECT * FROM attendance WHERE email = :email ORDER BY timeIn DESC")
    fun getAttendanceHistory(email: String): Flow<List<Attendance>>

    @Query("SELECT * FROM attendance WHERE email = :email ORDER BY timeIn DESC LIMIT 1")
    suspend fun getLatestAttendanceForUser(email: String): Attendance?
}