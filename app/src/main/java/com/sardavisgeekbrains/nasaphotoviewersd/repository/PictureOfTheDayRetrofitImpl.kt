package com.sardavisgeekbrains.nasaphotoviewersd.repository

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PictureOfTheDayRetrofitImpl {
    companion object {
        private const val BASE_URL = "https://api.nasa.gov/"
    }

    fun getPictureOfTheDay(): PictureOfTheDayAPI {
        val pictureOfTheDayRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        return pictureOfTheDayRetrofit.create(PictureOfTheDayAPI::class.java)
    }

    fun getMarsPictureByDate(): PictureOfTheDayAPI {
        val pictureOfTheDayRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        return pictureOfTheDayRetrofit.create(PictureOfTheDayAPI::class.java)
    }

    fun getEPIC(): PictureOfTheDayAPI {
        val pictureOfTheDayRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        return pictureOfTheDayRetrofit.create(PictureOfTheDayAPI::class.java)
    }


}