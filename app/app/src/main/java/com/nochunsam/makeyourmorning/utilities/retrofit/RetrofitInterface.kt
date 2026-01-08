package com.nochunsam.makeyourmorning.utilities.retrofit

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

data class DayRecordRequest(
    val date: String,
    val minute: Long
)

data class DayRecordResponse(
    val id: Long,
    val date: String,
    val minute: Long,
    val userId: String,
    val uuid: String
)

interface RetrofitInterface {
    @GET("/test")
    suspend fun getTest(): String

    @POST("/day-record/create")
    suspend fun createDayRecord(
        @Header("Authorization") token: String,
        @Body request: DayRecordRequest
    ): DayRecordResponse
}