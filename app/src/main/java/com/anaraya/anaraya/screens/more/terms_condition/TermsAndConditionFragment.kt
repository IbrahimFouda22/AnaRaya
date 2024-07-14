package com.anaraya.anaraya.screens.more.terms_condition

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
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentTermsAndConditionBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TermsAndConditionFragment : Fragment() {
    private lateinit var binding: FragmentTermsAndConditionBinding
    val viewModel by viewModels<TermsAndConditionViewModel>({ this })

    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTermsAndConditionBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.termsAndConditionUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
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
    override fun onStart() {
        super.onStart()
        showToolBar(requireActivity(),true)
        showBottomNavBar(requireActivity(),false)
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        showToolBar(requireActivity(),true)
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

}