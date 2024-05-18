package com.anaraya.anaraya.home.more.family.family_referrals

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
import com.anaraya.anaraya.databinding.FragmentReferralsBinding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReferralsFragment : Fragment() {
    private val viewModel by viewModels<FamilyReferralsViewModel>({ this })
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private lateinit var binding: FragmentReferralsBinding
    private lateinit var adapter: FamilyReferralsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentReferralsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        adapter = FamilyReferralsAdapter()
        binding.recyclerFamilyReferrals.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.familyReferralsUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.listFamilyReferrals.isNotEmpty()) {
                    adapter.submitList(it.listFamilyReferrals)
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
        showToolBar(requireActivity(), true)
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }
}