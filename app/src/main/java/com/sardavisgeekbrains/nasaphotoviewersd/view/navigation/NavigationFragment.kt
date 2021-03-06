package com.sardavisgeekbrains.nasaphotoviewersd.view.navigation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sardavisgeekbrains.nasaphotoviewersd.R
import com.sardavisgeekbrains.nasaphotoviewersd.databinding.FragmentNavigationBinding
import com.sardavisgeekbrains.nasaphotoviewersd.view.MainActivity
import com.sardavisgeekbrains.nasaphotoviewersd.view.layout.CoordinatorFragment
import com.sardavisgeekbrains.nasaphotoviewersd.view.picture.ApiFragment
import com.sardavisgeekbrains.nasaphotoviewersd.view.picture.PictureOfTheDayFragment

class NavigationFragment : Fragment() {
    lateinit var navigation: Navigation

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigation = (context as MainActivity).navigation
    }

    private var _binding: FragmentNavigationBinding? = null
    private val binding: FragmentNavigationBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNavigationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_earth -> navigation.showFragment(
                    PictureOfTheDayFragment.newInstance(),
                    true
                )
                R.id.action_mars -> navigation.showFragment(ApiFragment.newInstance(), true)
                R.id.action_animation -> navigation.showFragment(CoordinatorFragment.newInstance(), true)
            }
            true
        }
        binding.navigationView.selectedItemId = R.id.action_earth;
    }

    companion object {
        fun newInstance(): NavigationFragment {
            return NavigationFragment()
        }
    }
}