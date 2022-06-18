package com.sardavisgeekbrains.nasaphotoviewersd.view

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sardavisgeekbrains.nasaphotoviewersd.view.picture.EarthEpicFragment
import com.sardavisgeekbrains.nasaphotoviewersd.view.picture.MarsFragment

class ViewPager2Adapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = fragments.size

    private val fragments = arrayOf(MarsFragment(), EarthEpicFragment())
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}