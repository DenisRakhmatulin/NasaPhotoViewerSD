package com.sardavisgeekbrains.nasaphotoviewersd.viewmodel

import com.sardavisgeekbrains.nasaphotoviewersd.repository.PictureOfTheDayResponseData

sealed class PictureOfTheDayAppState{
    data class Success(val pictureOfTheDayResponseData: PictureOfTheDayResponseData):PictureOfTheDayAppState()
    data class Error(val error:Throwable):PictureOfTheDayAppState()
    data class Loading(val progress:Int?):PictureOfTheDayAppState()
}
