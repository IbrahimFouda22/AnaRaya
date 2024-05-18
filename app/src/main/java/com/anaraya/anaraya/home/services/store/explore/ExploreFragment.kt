package com.anaraya.anaraya.home.services.store.explore

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
import com.anaraya.anaraya.databinding.FragmentExploreBinding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.anaraya.anaraya.home.services.store.adapter.ExploreCategoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploreFragment(private val isProducts: Boolean) : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private val viewModel by viewModels<ExploreViewModel>()
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        val adapterCategory = ExploreCategoryAdapter()
        binding.recyclerCategoriesHome.adapter = adapterCategory

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exploreUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.exploreCategoriesList.isNotEmpty()) {
                    adapterCategory.submitList(it.exploreCategoriesList)
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
        viewModel.getAllData(isProducts)
        sharedViewModel.reloadClickDone()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllData(isProducts)
    }

}