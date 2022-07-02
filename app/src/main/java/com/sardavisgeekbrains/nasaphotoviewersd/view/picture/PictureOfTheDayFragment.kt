package com.sardavisgeekbrains.nasaphotoviewersd.view.picture

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.*
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.sardavisgeekbrains.nasaphotoviewersd.R
import com.sardavisgeekbrains.nasaphotoviewersd.databinding.FragmentPictureOfTheDayBinding
import com.sardavisgeekbrains.nasaphotoviewersd.view.settings.SettingsFragment
import com.sardavisgeekbrains.nasaphotoviewersd.viewmodel.PictureOfTheDayAppState
import com.sardavisgeekbrains.nasaphotoviewersd.viewmodel.PictureOfTheDayViewModel
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


class PictureOfTheDayFragment : Fragment() {

    var onImageClicked = false

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
                        if (onImageClicked) {
                            BottomSheetBehavior.from(binding.lifeHack.bottomSheetContainer).state =
                                BottomSheetBehavior.STATE_HIDDEN
                        } else {
                            BottomSheetBehavior.from(binding.lifeHack.bottomSheetContainer).state =
                                BottomSheetBehavior.STATE_COLLAPSED
                        }

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


        binding.imageView.setOnClickListener {
            onImageClicked = !onImageClicked

            val transitionChipSlide = Slide(Gravity.START)
            val transitionFadeWiki = Fade()
            val transitionImageTransform = ChangeImageTransform()
            transitionImageTransform.duration = 1500
            transitionChipSlide.duration = 2000
            transitionFadeWiki.duration = 2500

            transitionChipSlide.excludeTarget(binding.inputLayout, true)
            transitionChipSlide.excludeTarget(binding.imageView, true)
            transitionFadeWiki.excludeTarget(binding.chipGroup, true)


            val transitionSet = TransitionSet()
            transitionSet.addTransition(transitionChipSlide)
            transitionSet.addTransition(transitionImageTransform)
            transitionSet.addTransition(transitionFadeWiki)
            TransitionManager.beginDelayedTransition(binding.root, transitionSet)

            if (onImageClicked) {
                binding.chipGroup.visibility = View.GONE
            } else {
                binding.chipGroup.visibility = View.VISIBLE
            }

            if (onImageClicked) {
                binding.inputLayout.visibility = View.GONE
            } else {
                binding.inputLayout.visibility = View.VISIBLE
            }

            if (onImageClicked) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            binding.imageView.scaleType = if (onImageClicked) {
                ImageView.ScaleType.CENTER_CROP
            } else {
                ImageView.ScaleType.CENTER_INSIDE
            }
            val params = (binding.imageView.layoutParams as CoordinatorLayout.LayoutParams)
            params.height = if (onImageClicked) {
                FrameLayout.LayoutParams.MATCH_PARENT
            } else {
                FrameLayout.LayoutParams.WRAP_CONTENT
            }
            binding.imageView.layoutParams = params


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

                val textSpannable = pictureOfTheDayAppState.pictureOfTheDayResponseData.explanation
                var spannableStringBuilder: SpannableStringBuilder =
                    SpannableStringBuilder(textSpannable)

                binding.lifeHack.explanation.setText(
                    spannableStringBuilder,
                    TextView.BufferType.EDITABLE
                )
                spannableStringBuilder = binding.lifeHack.explanation.text as SpannableStringBuilder

                var n = 0


                for (i in 0 until spannableStringBuilder.length) {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(TimeUnit.MILLISECONDS.toMillis(300))
                        withContext(Dispatchers.Main) {

                            if (n > 6) {
                                n = 0
                            }

                            when (n) {
                                0 -> {
                                    spannableStringBuilder.setSpan(
                                        ForegroundColorSpan(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.red
                                            )
                                        ),
                                        i, i + 1, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                }
                                1 -> {
                                    spannableStringBuilder.setSpan(
                                        ForegroundColorSpan(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.orange
                                            )
                                        ),
                                        i, i + 1, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                }
                                2 -> {
                                    spannableStringBuilder.setSpan(
                                        ForegroundColorSpan(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.yellow
                                            )
                                        ),
                                        i, i + 1, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                }
                                3 -> {
                                    spannableStringBuilder.setSpan(
                                        ForegroundColorSpan(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.green
                                            )
                                        ),
                                        i, i + 1, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                }
                                4 -> {
                                    spannableStringBuilder.setSpan(
                                        ForegroundColorSpan(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.cyan
                                            )
                                        ),
                                        i, i + 1, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                }
                                5 -> {
                                    spannableStringBuilder.setSpan(
                                        ForegroundColorSpan(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.blue
                                            )
                                        ),
                                        i, i + 1, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                }
                                6 -> {
                                    spannableStringBuilder.setSpan(
                                        ForegroundColorSpan(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.violet
                                            )
                                        ),
                                        i, i + 1, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                }
                            }
                            n++


                        }
                    }
                }


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