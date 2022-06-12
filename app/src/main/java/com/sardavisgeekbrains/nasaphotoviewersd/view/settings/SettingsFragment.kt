package com.sardavisgeekbrains.nasaphotoviewersd.view.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.sardavisgeekbrains.nasaphotoviewersd.databinding.FragmentSettingsBinding
import com.sardavisgeekbrains.nasaphotoviewersd.view.MainActivity
import com.sardavisgeekbrains.nasaphotoviewersd.view.ThemeOne
import com.sardavisgeekbrains.nasaphotoviewersd.view.ThemeSecond
import com.sardavisgeekbrains.nasaphotoviewersd.view.ThemeThird

class SettingsFragment : Fragment() {

    private lateinit var parentActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = requireActivity() as MainActivity
    }

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nightModeBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        when (parentActivity.getCurrentTheme()) {
            1 -> binding.tabLayout.setScrollPosition(0, 0f, true)
            2 -> binding.tabLayout.setScrollPosition(1, 0f, true)
            3 -> binding.tabLayout.setScrollPosition(2, 0f, true)
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                //Toast.makeText(requireContext(), "${tab?.position}", Toast.LENGTH_SHORT).show()
                themeSelect(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                themeSelect(tab)
            }
        })

        setHasOptionsMenu(true)


    }


    fun themeSelect(tab: TabLayout.Tab?) {
        when (tab?.position) {
            0 -> {
                parentActivity.setCurrentTheme(ThemeOne)
                parentActivity.recreate()
            }
            1 -> {
                parentActivity.setCurrentTheme(ThemeSecond)
                parentActivity.recreate()
            }
            2 -> {
                parentActivity.setCurrentTheme(ThemeThird)
                parentActivity.recreate()
            }
        }
    }

    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)


    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}