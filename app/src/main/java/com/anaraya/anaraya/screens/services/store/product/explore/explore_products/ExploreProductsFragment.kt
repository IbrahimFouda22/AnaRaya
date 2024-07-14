package com.anaraya.anaraya.screens.services.store.product.explore.explore_products

import android.annotation.SuppressLint
import android.content.SharedPreferences
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentExploreProductsBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.services.store.adapter.ExploreProductAdapter
import com.anaraya.anaraya.screens.services.store.interaction.ExploreProductInteraction
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ExploreProductsFragment : Fragment(), ExploreProductInteraction {
    private lateinit var binding: FragmentExploreProductsBinding
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val navArgs: ExploreProductsFragmentArgs by navArgs()
    private var count = 0
    private lateinit var catList: List<CategoryUiState>
    private lateinit var catMap: MutableMap<Int, String>

    //to know if i selected cat or all to know if internet is not available and make refresh
    private var categoryId = -1
    private lateinit var adapter: ExploreProductAdapter

    @Inject
    lateinit var factory: ExploreProductViewModel.AssistedFactory
    private val viewModel: ExploreProductViewModel by viewModels {
        ExploreProductViewModel.createProvider(
            navArgs.categoryId,
            navArgs.categoryName,
            factory
        )
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExploreProductsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        adapter = ExploreProductAdapter(this)
        binding.recyclerExploreProduct.adapter = adapter

        lifecycleScope.launch {
            viewModel.products.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.products != null) {
                    it.products.collectLatest { data ->
                        adapter.submitData(data)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.categories.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.subCategories.isNotEmpty()) {
                    catList = it.subCategories
                    catMap = mutableMapOf()
                    count = 0
                    it.subCategories.forEach { item ->
                        binding.chipExploreFilter.addView(createChip(item.name, count))
                        count++
                    }
                    when (categoryId) {
                        -1 -> {
                            binding.chipExploreFilter.check(R.id.chipExploreAll)
                        }

                        else -> {
                            binding.chipExploreFilter.check(navArgs.categoryId)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collect {
                viewModel.showLoading(it.refresh is LoadState.Loading)
            }
        }

        adapter.addLoadStateListener { loadState ->
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                viewModel.showLoading(false)
                if (it.error.message == getString(R.string.not_found)) {
                    if (adapter.snapshot().isEmpty())
                        viewModel.setError(it.error.message)
                } else {
                    viewModel.setError(it.error.message)
                }
            }
        }

        binding.chipExploreFilter.setOnCheckedStateChangeListener(onChipSelectedListener())

        binding.btnBackAllExploreProduct.setOnClickListener {
            findNavController().popBackStack()
        }

        btnBack.setOnClickListener {
            reload()
        }

        btnReload.setOnClickListener {
            reload()
        }

        return binding.root
    }

    override fun onClick(exploreProductUiDetails: ExploreProductUiDetails) {
        findNavController().navigate(
            ExploreProductsFragmentDirections.actionExploreProductsFragmentToExploreProductDetailsFragment(
                exploreProductUiDetails
            )
        )
    }

    override fun onStart() {
        super.onStart()
        showCardHome(requireActivity(), false)
        showBottomNavBar(requireActivity(), false)
        if(sharedPreferences.getString("lang","en") == "ar")
            binding.chipExploreFilter.layoutDirection = View.LAYOUT_DIRECTION_RTL
        else
            binding.chipExploreFilter.layoutDirection = View.LAYOUT_DIRECTION_LTR

    }

    @SuppressLint("InflateParams")
    private fun createChip(chipName: String, position: Int): Chip {
        val chip = layoutInflater.inflate(R.layout.item_chip, null, false) as Chip
        chip.id = catList[position].id
        chip.text = chipName
        catMap[chip.id] = chipName
        return chip
    }

    private fun onChipSelectedListener(): ChipGroup.OnCheckedStateChangeListener {
        return ChipGroup.OnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                when (checkedIds[0]) {
                    R.id.chipExploreAll -> {
                        categoryId = -1
                        viewModel.showLoading(true)
                        viewModel.setCatName(getString(R.string.all))
                        viewModel.getAllProduct()
                    }

                    else -> {
                        categoryId = checkedIds[0]
                        viewModel.showLoading(true)
                        viewModel.setCatName(catMap[categoryId]!!)
                        viewModel.getProductBySubCat(checkedIds[0])
                    }

                }
            }
        }
    }


    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllData(categoryId)
        binding.chipExploreFilter.check(navArgs.categoryId)
        sharedViewModel.reloadClickDone()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

}