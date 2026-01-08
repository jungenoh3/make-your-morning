package com.nochunsam.makeyourmorning.utilities.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.nochunsam.makeyourmorning.common.data.DayRecord
import com.nochunsam.makeyourmorning.utilities.database.dao.DayRecordDao
import com.nochunsam.makeyourmorning.utilities.retrofit.RetrofitInterface
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

class AppRepository(
    private val dayRecordDao: DayRecordDao,
    private val apiService: RetrofitInterface
    ) {
    val auth = FirebaseAuth.getInstance()

    fun getDayCount(): Flow<Int> = dayRecordDao.getRecordCount()
    fun getDayRecord(): Flow<List<DayRecord>> = dayRecordDao.getAllRecords()
    fun getEarliestRecordPerDay(): Flow<List<DayRecord>> = dayRecordDao.getEarliestRecordPerDay()
    suspend fun insertDayRecord(minute: Int) {

        val response = apiService.getTest()
        Log.d("API_TEST", "서버 응답: ${response}")

        val instant = Instant.now().minus(minute.toLong(), ChronoUnit.MINUTES)
        val startDate = Date.from(instant)

//        if (auth.currentUser != null && auth.currentUser?.isAnonymous == false) {
//
//        }

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