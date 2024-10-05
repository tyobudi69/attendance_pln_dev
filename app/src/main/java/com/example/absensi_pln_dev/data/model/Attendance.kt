package com.example.absensi_pln_dev.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val timeIn: Long,
    var timeOut: Long? = null,
    val timeInLatitude: Double? = null,
    val timeInLongitude: Double? = null,
    var timeOutLatitude: Double? = null,
    var timeOutLongitude: Double? = null,
    val timeInImageUri: String? = null,
    var timeOutImageUri: String? = null
)