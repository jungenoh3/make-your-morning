package com.nochunsam.makeyourmorning.utilities.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nochunsam.makeyourmorning.common.data.DayCount
import kotlinx.coroutines.flow.Flow

@Dao
interface DayCountDao {
    @Query("SELECT * FROM DayCount WHERE id = 1")
    suspend fun get(): DayCount?

    @Query("SELECT * FROM DayCount WHERE id = 1")
    fun getFlow(): Flow<DayCount>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dayCount: DayCount)

    @Query("UPDATE DayCount SET count = count + 1 WHERE id = 1")
    suspend fun increment()
}