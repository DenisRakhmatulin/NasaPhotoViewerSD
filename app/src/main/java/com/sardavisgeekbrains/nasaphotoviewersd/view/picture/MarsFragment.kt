package com.sardavisgeekbrains.nasaphotoviewersd.view.picture

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.sardavisgeekbrains.nasaphotoviewersd.R
import com.sardavisgeekbrains.nasaphotoviewersd.databinding.FragmentMarsBinding
import com.sardavisgeekbrains.nasaphotoviewersd.view.settings.SettingsFragment
import com.sardavisgeekbrains.nasaphotoviewersd.viewmodel.PictureOfTheDayAppState
import com.sardavisgeekbrains.nasaphotoviewersd.viewmodel.PictureOfTheDayViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MarsFragment : Fragment() {

    private var _binding: FragmentMarsBinding? = null
    private val binding: FragmentMarsBinding
        get() {
            return _binding!!
        }

    @RequiresApi(Build.VERSION_CODES.O)
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @RequiresApi(Build.VERSION_CODES.O)
    var today = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    var tdby = today.minusDays(2).format(formatter)

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                openFragment(SettingsFragment.newInstance())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.apply {
            beginTransaction()
                .add(R.id.contentContainer, fragment)
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, { render(it) })
        viewModel.sendMarsRequest(tdby.toString())

        setHasOptionsMenu(true)

    }

    private fun render(appState: PictureOfTheDayAppState) {
        when (appState) {
            is PictureOfTheDayAppState.Error -> {
                binding.loadingImageView.visibility = View.GONE
                Snackbar.make(binding.root, appState.error.toString(), Snackbar.LENGTH_SHORT).show()
            }
            is PictureOfTheDayAppState.Loading -> {
                binding.loadingImageView.visibility = View.VISIBLE
                binding.loadingImageView.load(R.drawable.progress_animation)
            }
            is PictureOfTheDayAppState.SuccessMars -> {
                if (appState.serverResponseData.photos.isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        "?? ???????? ???????? curiosity ???? ???????????? ???? ???????????? ????????????",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    val url = appState.serverResponseData.photos.first().imgSrc
                    binding.loadingImageView.load(url)
                }

            }
        }
    }


    companion object {
        fun newInstance(): MarsFragment {
            return MarsFragment()
        }
    }


}