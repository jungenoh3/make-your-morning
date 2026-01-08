package com.nochunsam.makeyourmorning.utilities.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.nochunsam.makeyourmorning.common.data.DayRecord
import com.nochunsam.makeyourmorning.utilities.database.dao.DayRecordDao
import com.nochunsam.makeyourmorning.utilities.retrofit.DayRecordRequest
import com.nochunsam.makeyourmorning.utilities.retrofit.RetrofitInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

class AppRepository(
    private val dayRecordDao: DayRecordDao,
    private val apiService: RetrofitInterface
    ) {
    val auth = FirebaseAuth.getInstance()
    val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault())

    fun getDayCount(): Flow<Int> = dayRecordDao.getRecordCount()
    fun getDayRecord(): Flow<List<DayRecord>> = dayRecordDao.getAllRecords()
    fun getEarliestRecordPerDay(): Flow<List<DayRecord>> = dayRecordDao.getEarliestRecordPerDay()
    suspend fun insertDayRecord(minute: Int) {

        val instant = Instant.now().minus(minute.toLong(), ChronoUnit.MINUTES)
        val startDate = Date.from(instant)

        if (auth.currentUser != null && auth.currentUser?.isAnonymous == false) {
            try {
                val tokenResult = auth.currentUser?.getIdToken(true)?.await()
                val dateString = isoFormatter.format(startDate)
                val request = DayRecordRequest(
                    date = dateString,
                    minute = minute.toLong()
                )
                val response = apiService.createDayRecord(
                    "Bearer ${tokenResult?.token}",
                    request
                )
                Log.d("API_SUCCESS", "저장 완료 ID: ${response.id}")
            } catch (e: Exception) {
                Log.e("API_ERROR", "서버 저장 실패: ${e.message}")
            }
        }

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