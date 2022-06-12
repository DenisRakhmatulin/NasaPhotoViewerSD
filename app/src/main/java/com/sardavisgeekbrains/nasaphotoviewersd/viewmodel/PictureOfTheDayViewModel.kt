package com.sardavisgeekbrains.nasaphotoviewersd.viewmodel

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sardavisgeekbrains.nasaphotoviewersd.BuildConfig
import com.sardavisgeekbrains.nasaphotoviewersd.repository.MarsPhotosServerResponseData
import com.sardavisgeekbrains.nasaphotoviewersd.repository.PictureOfTheDayResponseData
import com.sardavisgeekbrains.nasaphotoviewersd.repository.PictureOfTheDayRetrofitImpl
import com.sardavisgeekbrains.nasaphotoviewersd.viewmodel.PictureOfTheDayAppState.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PictureOfTheDayViewModel(
    private val liveData: MutableLiveData<PictureOfTheDayAppState> = MutableLiveData(),
    private val pictureOfTheDayRetrofitImpl: PictureOfTheDayRetrofitImpl = PictureOfTheDayRetrofitImpl()
) : ViewModel() {

    fun getLiveData(): LiveData<PictureOfTheDayAppState> {
        return liveData
    }

    fun sendRequest(date: String) {
        liveData.postValue(Loading(null))

        try {
            pictureOfTheDayRetrofitImpl.getPictureOfTheDay()
                .getPictureOfTheDay(date, BuildConfig.NASA_API_KEY)
                .enqueue(callback)
        } catch (e: Throwable) {
            liveData.postValue(Error(throw IllegalStateException(API_ERROR)))
        }

    }

    fun sendMarsRequest(date: String) {
        liveData.postValue(Loading(null))
        try {
            pictureOfTheDayRetrofitImpl.getMarsPictureByDate()
                .getMarsImageByDate(date, BuildConfig.NASA_API_KEY)
                .enqueue(marsCallback)
        } catch (e: Throwable) {
            liveData.postValue(Error(throw IllegalStateException(API_ERROR)))
        }

    }


    private val callback = object : Callback<PictureOfTheDayResponseData> {
        override fun onResponse(
            call: Call<PictureOfTheDayResponseData>,
            response: Response<PictureOfTheDayResponseData>
        ) {
            if (response.isSuccessful && response.body() != null) {
                liveData.postValue(Success(response.body()!!))
            } else {
                val message = response.message()
                if (message.isNullOrEmpty()) {
                    liveData.postValue(Error(Throwable(UNKNOWN_ERROR)))
                } else {
                    liveData.postValue(Error(Throwable(message)))
                }
            }
        }

        override fun onFailure(call: Call<PictureOfTheDayResponseData>, t: Throwable) {
            liveData.postValue(Error(t))
        }

    }

    val marsCallback = object : Callback<MarsPhotosServerResponseData> {

        override fun onResponse(
            call: Call<MarsPhotosServerResponseData>,
            response: Response<MarsPhotosServerResponseData>,
        ) {
            if (response.isSuccessful && response.body() != null) {
                liveData.postValue(SuccessMars(response.body()!!))
            } else {
                val message = response.message()
                if (message.isNullOrEmpty()) {
                    liveData.postValue(Error(Throwable(UNKNOWN_ERROR)))
                } else {
                    liveData.postValue(Error(Throwable(message)))
                }
            }
        }

        override fun onFailure(call: Call<MarsPhotosServerResponseData>, t: Throwable) {
            liveData.postValue(Error(t))
        }
    }


    /*fun getDayBeforeYesterday(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val yesterday = LocalDateTime.now().minusDays(2)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return yesterday.format(formatter)
        } else {
            val cal: Calendar = Calendar.getInstance()
            val s = SimpleDateFormat("yyyy-MM-dd")
            cal.add(Calendar.DAY_OF_YEAR, -2)
            return s.format(cal.time)
        }
    }*/

    companion object {
        private const val API_ERROR = "You need API Key"
        private const val UNKNOWN_ERROR = "Unidentified error"
    }

}