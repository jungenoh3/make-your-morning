package com.nochunsam.makeyourmorning.utilities.database

import com.nochunsam.makeyourmorning.common.data.DayRecord
import com.nochunsam.makeyourmorning.utilities.database.dao.DayRecordDao
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

class AppRepository(private val dayRecordDao: DayRecordDao) {

    fun getDayCount(): Flow<Int> = dayRecordDao.getRecordCount()
    fun getDayRecord(): Flow<List<DayRecord>> = dayRecordDao.getAllRecords()
    fun getEarliestRecordPerDay(): Flow<List<DayRecord>> = dayRecordDao.getEarliestRecordPerDay()
    suspend fun insertDayRecord(minute: Int) {
        val instant = Instant.now().minus(minute.toLong(), ChronoUnit.MINUTES)
        val startDate = Date.from(instant)

        val record = DayRecord(
            date = startDate,
            minute = minute
        )
        dayRecordDao.insert(record)
    }
    suspend fun truncateTable() {
        dayRecordDao.truncateTable()
    }
}