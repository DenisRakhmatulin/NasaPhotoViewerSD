package com.sardavisgeekbrains.nasaphotoviewersd.viewmodel

import com.example.nasaapp.model.data.EarthEpicServerResponseData
import com.sardavisgeekbrains.nasaphotoviewersd.repository.MarsPhotosServerResponseData
import com.sardavisgeekbrains.nasaphotoviewersd.repository.PictureOfTheDayResponseData

sealed class PictureOfTheDayAppState {
    data class Success(val pictureOfTheDayResponseData: PictureOfTheDayResponseData) :
        PictureOfTheDayAppState()

    data class SuccessMars(val serverResponseData: MarsPhotosServerResponseData) :
        PictureOfTheDayAppState()

    data class SuccessEarthEpic(val serverResponseData: List<EarthEpicServerResponseData>) :
        PictureOfTheDayAppState()

    data class Error(val error: Throwable) : PictureOfTheDayAppState()
    data class Loading(val progress: Int?) : PictureOfTheDayAppState()
}
