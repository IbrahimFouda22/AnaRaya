package com.anaraya.anaraya.home.more.feedback.question2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentQuesion2Binding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.anaraya.anaraya.home.more.feedback.FeedBackViewModel
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Question2Fragment : Fragment() {
    private lateinit var binding: FragmentQuesion2Binding
    val viewModel by viewModels<Question2ViewModel>({ this })
    private val feedBackViewModel by viewModels<FeedBackViewModel>({ requireActivity() })

    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuesion2Binding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.question2UiState.collectLatest {
                if (it.error != null) {
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                    sharedViewModel.setError(it.error)
                }
                if(it.isSucceedAddFeedBack){
                    if (it.messageAddFeedBack!=null)
                        Toast.makeText(context, it.messageAddFeedBack, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnSubmitFeedBack.setOnClickListener {
            if (feedBackViewModel.feedBackState.value.rate < 1) {
                Toast.makeText(context, getString(R.string.please_rate_us), Toast.LENGTH_SHORT)
                    .show()
                feedBackViewModel.setStateViewPager(0)
            } else
                viewModel.addFeedBack(
                    feedBackViewModel.feedBackState.value.rate,
                    binding.txtAreaFeedBack.text.toString()
                )

        }

        btnBack.setOnClickListener {
            reload()
        }
        btnReload.setOnClickListener {
            reload()
        }

        return binding.root
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        showToolBar(requireActivity(),true)
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

}