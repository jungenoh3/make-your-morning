package com.nochunsam.makeyourmorning.utilities.database

import android.app.Application
import com.nochunsam.makeyourmorning.common.data.DayCount
import com.nochunsam.makeyourmorning.utilities.database.dao.DayCountDao
import kotlinx.coroutines.flow.Flow

class AppRepository(application: Application) {
    private val appDatabase = AppDatabase.getInstance(application)!!
    private val dayCountDao: DayCountDao = appDatabase.dayCountDao()

    fun getDayCount(): Flow<DayCount> = dayCountDao.getFlow()
    suspend fun increaseDayCount() = dayCountDao.increment()
}