package com.anaraya.anaraya.screens.more.help.help_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentHelpDetailsBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.more.help.help_details.adapter.HelpDetailsAdapter
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HelpDetailsFragment : Fragment() {
    private val navArgs by navArgs<HelpDetailsFragmentArgs>()
    private lateinit var binding: FragmentHelpDetailsBinding
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val viewModel by viewModels<HelpDetailsViewModel>({ this })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHelpDetailsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.problem = navArgs.topic.topic
        viewModel.getAllData(navArgs.topic.id)

        val adapter = HelpDetailsAdapter()
        binding.recyclerHelpDetails.adapter = adapter


        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.helpUiState.collectLatest {

                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if(it.helpUiStateList.isNotEmpty()){
                    adapter.submitList(it.helpUiStateList)
                }
            }
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
        viewModel.getAllData(navArgs.topic.id)
        sharedViewModel.reloadClickDone()
    }

}