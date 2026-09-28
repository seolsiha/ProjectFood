package com.example.projectfood.data

import retrofit2.http.GET
import retrofit2.http.Query

interface HotPepperApi {
    @GET("hotpepper/gourmet/v1/")
    suspend fun searchShops(
        @Query("key") key: String,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("range") range: Int,
        @Query("format") format: String = "json",
        @Query("count") count: Int = 20
    ): GourmetResponse
}