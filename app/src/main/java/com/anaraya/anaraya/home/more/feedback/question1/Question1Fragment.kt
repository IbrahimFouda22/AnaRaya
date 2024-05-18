package com.anaraya.anaraya.home.more.feedback.question1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentQuestion1Binding
import com.anaraya.anaraya.home.more.feedback.FeedBackViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Question1Fragment : Fragment() {

    private lateinit var binding:FragmentQuestion1Binding
    val viewModel by viewModels<Question1ViewModel> ({this})
    private val feedBackViewModel by viewModels<FeedBackViewModel> ({requireActivity()})

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuestion1Binding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.btnNextFeedBack.setOnClickListener {
            feedBackViewModel.setRate(viewModel.question1UiState.value.rate)
            feedBackViewModel.setStateViewPager(1)
            feedBackViewModel.resetStateViewPager()
        }

        return binding.root
    }


}