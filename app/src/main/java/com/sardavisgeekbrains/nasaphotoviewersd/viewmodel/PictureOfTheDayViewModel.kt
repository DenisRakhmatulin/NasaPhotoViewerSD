package com.sardavisgeekbrains.nasaphotoviewersd.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sardavisgeekbrains.nasaphotoviewersd.BuildConfig
import com.sardavisgeekbrains.nasaphotoviewersd.repository.PictureOfTheDayResponseData
import com.sardavisgeekbrains.nasaphotoviewersd.repository.PictureOfTheDayRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureOfTheDayViewModel(
    private val liveData: MutableLiveData<PictureOfTheDayAppState> = MutableLiveData(),
    private val pictureOfTheDayRetrofitImpl: PictureOfTheDayRetrofitImpl = PictureOfTheDayRetrofitImpl()
) : ViewModel() {

    fun getLiveData(): LiveData<PictureOfTheDayAppState> {
        return liveData
    }

    fun sendRequest(date: String) {
        liveData.postValue(PictureOfTheDayAppState.Loading(null))

        try {
            pictureOfTheDayRetrofitImpl.getRetrofit()
                .getPictureOfTheDay(date, BuildConfig.NASA_API_KEY)
                .enqueue(callback)
        } catch (e: Throwable) {
            liveData.postValue(PictureOfTheDayAppState.Error(throw IllegalStateException("где-то потерялся ключ, свяжитесь с нами")))
        }

    }


    private val callback = object : Callback<PictureOfTheDayResponseData> {
        override fun onResponse(
            call: Call<PictureOfTheDayResponseData>,
            response: Response<PictureOfTheDayResponseData>
        ) {
            if (response.isSuccessful) {
                response.body()?.let {
                    liveData.postValue(PictureOfTheDayAppState.Success(it))
                }
            } else {
                // TODO HW
            }
        }

        override fun onFailure(call: Call<PictureOfTheDayResponseData>, t: Throwable) {
            // TODO HW
        }

    }

}