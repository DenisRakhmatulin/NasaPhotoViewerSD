package com.sardavisgeekbrains.nasaphotoviewersd.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sardavisgeekbrains.nasaphotoviewersd.R
import com.sardavisgeekbrains.nasaphotoviewersd.view.picture.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container,
                PictureOfTheDayFragment.newInstance()).commit()
        }
    }
}