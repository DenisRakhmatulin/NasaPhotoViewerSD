package com.sardavisgeekbrains.nasaphotoviewersd.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sardavisgeekbrains.nasaphotoviewersd.R
import com.sardavisgeekbrains.nasaphotoviewersd.view.picture.PictureOfTheDayFragment

const val ThemeOne = 1
const val ThemeSecond = 2
const val ThemeThird = 3

class MainActivity : AppCompatActivity() {

    private val KEY_SP = "sp"
    private val KEY_CURRENT_THEME = "current_theme"

    var navigation = Navigation(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getRealStyle(getCurrentTheme()))
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            navigation.showNavigationFragment(NavigationFragment.newInstance())
            navigation.showFragment(PictureOfTheDayFragment.newInstance(), false)
        }
    }


    fun setCurrentTheme(currentTheme: Int) {
        val sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_CURRENT_THEME, currentTheme)
        editor.apply()
    }

    fun getCurrentTheme(): Int {
        val sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_CURRENT_THEME, -1)
    }

    private fun getRealStyle(currentTheme: Int): Int {
        return when (currentTheme) {
            ThemeOne -> R.style.MyRedTheme
            ThemeSecond -> R.style.MyGreenTheme
            ThemeThird -> R.style.MyBlueTheme
            else -> 0
        }
    }
}