package com.sardavisgeekbrains.nasaphotoviewersd.view.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sardavisgeekbrains.nasaphotoviewersd.R
import com.sardavisgeekbrains.nasaphotoviewersd.databinding.FragmentApiBinding
import com.sardavisgeekbrains.nasaphotoviewersd.view.ViewPager2Adapter

class ApiFragment : Fragment() {

    private var _binding: FragmentApiBinding? = null
    private val binding: FragmentApiBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApiBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = ViewPager2Adapter(this)


        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager,
            object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.text = when (position) {
                        0 -> "Curiosity Mars"
                        else -> "Earth PIC"
                    }
                    tab.setIcon(
                        when (position) {
                            0 -> R.drawable.ic_mars
                            else -> R.drawable.ic_earth
                        }
                    )
                }
            }).attach()
    }

    companion object {
        fun newInstance(): ApiFragment {
            return ApiFragment()
        }
    }


}