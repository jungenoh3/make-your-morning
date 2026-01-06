package com.nochunsam.makeyourmorning.utilities.database

import android.app.Application
import com.nochunsam.makeyourmorning.common.data.DayRecord
import com.nochunsam.makeyourmorning.utilities.database.dao.DayRecordDao
import kotlinx.coroutines.flow.Flow
import java.util.Date

class AppRepository(application: Application) {
    private val appDatabase = AppDatabase.getInstance(application)!!
    private val dayRecordDao: DayRecordDao = appDatabase.dayRecordDao()

    fun getDayCount(): Flow<Int> = dayRecordDao.getRecordCount()
    fun getDayRecord(): Flow<List<DayRecord>> = dayRecordDao.getAllRecords()
    suspend fun insertDayRecord(minute: Int) {
        val record = DayRecord(
            date = Date(),
            minute = minute
        )
        dayRecordDao.insert(record)
    }
}