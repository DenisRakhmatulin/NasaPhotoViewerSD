package com.sardavisgeekbrains.nasaphotoviewersd.repository

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PictureOfTheDayRetrofitImpl {
    private val nasaBaseUrl = "https://api.nasa.gov/"
    fun getRetrofit():PictureOfTheDayAPI {
        val pictureOfTheDayRetrofit = Retrofit.Builder()
            .baseUrl(nasaBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        return pictureOfTheDayRetrofit.create(PictureOfTheDayAPI::class.java)
    }
}