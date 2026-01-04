package com.nochunsam.makeyourmorning.common.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class DayCount(
    @PrimaryKey() val id: Int = 1,
    val count: Int
)
