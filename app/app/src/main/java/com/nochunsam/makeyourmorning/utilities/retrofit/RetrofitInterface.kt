package com.nochunsam.makeyourmorning.utilities.retrofit

import retrofit2.Call
import retrofit2.http.GET

public interface RetrofitInterface {
    @GET("/test")
    suspend fun getTest(): String
}