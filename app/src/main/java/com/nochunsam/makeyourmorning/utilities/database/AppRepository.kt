package com.nochunsam.makeyourmorning.utilities.database

import android.app.Application
import com.nochunsam.makeyourmorning.common.data.DayRecord
import com.nochunsam.makeyourmorning.utilities.database.dao.DayRecordDao
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

class AppRepository(application: Application) {
    private val appDatabase = AppDatabase.getInstance(application)!!
    private val dayRecordDao: DayRecordDao = appDatabase.dayRecordDao()

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