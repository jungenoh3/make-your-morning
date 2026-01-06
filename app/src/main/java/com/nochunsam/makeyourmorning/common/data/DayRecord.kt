package com.nochunsam.makeyourmorning.common.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class DayRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Date,
    val minute: Int
)