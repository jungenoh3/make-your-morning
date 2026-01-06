package com.nochunsam.makeyourmorning.utilities.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nochunsam.makeyourmorning.common.data.DayRecord
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface DayRecordDao {
    // 기록 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: DayRecord)

    // 모든 기록 가져오기 (날짜 최신순)
    @Query("SELECT * FROM DayRecord ORDER BY date DESC")
    fun getAllRecords(): Flow<List<DayRecord>>

    // 특정 날짜의 기록 가져오기 (예: 오늘 기록했는지 확인할 때)
    @Query("SELECT * FROM DayRecord WHERE date = :targetDate LIMIT 1")
    suspend fun getRecordByDate(targetDate: Date): DayRecord?

    // 개수 가져오기
    @Query("SELECT COUNT(*) FROM DayRecord")
    fun getRecordCount(): Flow<Int>

    // 기록 삭제
    @Query("DELETE FROM DayRecord WHERE id = :id")
    suspend fun deleteById(id: Int)
}