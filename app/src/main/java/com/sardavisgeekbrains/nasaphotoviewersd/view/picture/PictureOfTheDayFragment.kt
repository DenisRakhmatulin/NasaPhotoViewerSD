package com.sardavisgeekbrains.nasaphotoviewersd.view.picture

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.sardavisgeekbrains.nasaphotoviewersd.R
import com.sardavisgeekbrains.nasaphotoviewersd.databinding.FragmentPictureOfTheDayBinding
import com.sardavisgeekbrains.nasaphotoviewersd.view.settings.SettingsFragment
import com.sardavisgeekbrains.nasaphotoviewersd.viewmodel.PictureOfTheDayAppState
import com.sardavisgeekbrains.nasaphotoviewersd.viewmodel.PictureOfTheDayViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding: FragmentPictureOfTheDayBinding
        get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @RequiresApi(Build.VERSION_CODES.O)
    var today = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    var yesterday = today.minusDays(1).format(formatter)

    @RequiresApi(Build.VERSION_CODES.O)
    var tdby = today.minusDays(2).format(formatter)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
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

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.sendRequest(today.toString())

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.lifeHack.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED


        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {}
                    BottomSheetBehavior.STATE_COLLAPSED -> {}
                    BottomSheetBehavior.STATE_EXPANDED -> {}
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {}
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        BottomSheetBehavior.from(binding.lifeHack.bottomSheetContainer).state =
                            BottomSheetBehavior.STATE_COLLAPSED
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("@@@", "$slideOffset")
            }

        })

        setHasOptionsMenu(true)



        binding.chipGroup.setOnCheckedChangeListener { group, position ->
            val chip = group.findViewById<Chip>(position)
            val tag = chip.tag
            when (tag) {
                "chip1" -> {
                    viewModel.sendRequest(today.toString())
                }
                "chip2" -> {
                    viewModel.sendRequest(yesterday.toString())
                }
                "chip3" -> {
                    viewModel.sendRequest(tdby.toString())
                }
            }
        }


    }


    private fun renderData(pictureOfTheDayAppState: PictureOfTheDayAppState) {
        when (pictureOfTheDayAppState) {
            is PictureOfTheDayAppState.Error -> {
                Snackbar.make(
                    binding.root,
                    pictureOfTheDayAppState.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            is PictureOfTheDayAppState.Loading -> {}
            is PictureOfTheDayAppState.Success -> {
                binding.imageView.load(pictureOfTheDayAppState.pictureOfTheDayResponseData.url) {
                    placeholder(R.drawable.earth)
                }
                binding.lifeHack.title.text =
                    pictureOfTheDayAppState.pictureOfTheDayResponseData.title
                binding.lifeHack.explanation.text =
                    pictureOfTheDayAppState.pictureOfTheDayResponseData.explanation

            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PictureOfTheDayFragment()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}