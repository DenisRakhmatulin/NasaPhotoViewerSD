package com.sardavisgeekbrains.nasaphotoviewersd.repository

import com.example.nasaapp.model.data.EarthEpicServerResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureOfTheDayAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("date") date: String,
        @Query("api_key") apiKey: String
    ): Call<PictureOfTheDayResponseData>

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getMarsImageByDate(
        @Query("earth_date") earth_date: String,
        @Query("api_key") apiKey: String
    ): Call<MarsPhotosServerResponseData>

    @GET("EPIC/api/natural")
    fun getEPIC(@Query("api_key") apiKey: String): Call<List<EarthEpicServerResponseData>>
}