package com.anaraya.anaraya.screens.category_product

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentCategoryProductBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.category_product.adapter.CategoryProductAdapter
import com.anaraya.anaraya.screens.shared_interaction.ProductInteractionListener
import com.anaraya.anaraya.util.plusNumBasket
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CategoryProductFragment : Fragment(), ProductInteractionListener {

    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private lateinit var binding: FragmentCategoryProductBinding

    //private var allList: Flow<PagingData<ProductUiState>>? = null
    private lateinit var catList: List<CategoryUiState>
    private lateinit var catMap: MutableMap<Int, String>
    private val navArgs: CategoryProductFragmentArgs by navArgs()
    private var count = 0
    private var pos = -1
    //to know if i selected cat or all to know if internet is not available and make refresh
    private var categoryId = -1
    private lateinit var adapter: CategoryProductAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var factory: CategoryProductViewModel.AssistedFactory
    private val viewModel: CategoryProductViewModel by viewModels {
        CategoryProductViewModel.createProvider(
            navArgs.categoryId,
            navArgs.categoryName,
            factory
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryProductBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.sharedViewModel = sharedViewModel
        binding.lifecycleOwner = viewLifecycleOwner

//        categoryId = navArgs.categoryId

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        adapter = CategoryProductAdapter(this)
        binding.recyclerCategoryProduct.adapter = adapter

        binding.chipCategoryFilter.check(R.id.chipAll)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigateToCart.collectLatest {
                if (it) {
                    findNavController().navigate(CategoryProductFragmentDirections.actionCategoryProductFragmentToCartFragment())
                    viewModel.navigateToCartDone()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.products.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.products != null) {
                    it.products.collectLatest { data ->
                        adapter.submitData(data)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addCartState.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.addCartUiState != null) {
                    //Toast.makeText(context, it.addCartUiState, Toast.LENGTH_SHORT).show()
                    if(it.addCartSucceed){
                        adapter.changeIcon(pos)
                        adapter.notifyItemChanged(pos)
                        plusNumBasket(sharedPreferences, sharedViewModel, requireContext())
                        viewModel.resetState()
                        sharedViewModel.getCart()
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
                if (it.categories.isNotEmpty()) {
                    catList = it.categories
                    catMap = mutableMapOf()
                    count = 0
                    it.categories.forEach { item ->
                        binding.chipCategoryFilter.addView(createChip(item.name, count))
                        count++
                    }
//                    when (categoryId) {
//                        -1 -> {
//                            binding.chipCategoryFilter.check(R.id.chipAll)
//                        }
//                        else ->{
//                            binding.chipCategoryFilter.check(navArgs.categoryId)
//                        }
//                    }
                }
//                else{
//                    binding.chipCategoryFilter.check(R.id.chipAll)
//                }
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

        binding.chipCategoryFilter.setOnCheckedStateChangeListener(onChipSelectedListener())

        binding.btnBackAllCategoryProduct.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSearchAllCategoryProduct.setOnClickListener {
            findNavController().navigate(CategoryProductFragmentDirections.actionCategoryProductFragmentToSearchFragment())
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
        showCardHome(requireActivity(), false)
        showBottomNavBar(requireActivity(), false)
    }

    override fun onCLick(productId: Int) {
        findNavController().navigate(
            CategoryProductFragmentDirections.actionCategoryProductFragmentToProductDetailsFragment(
                productId
            )
        )
    }

    override fun addToCart(productId: Int, position: Int) {
        viewModel.addToCart(productId, 1)
        pos = position
        //adapter.notifyItemChanged(15)
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
                    R.id.chipAll -> {
                        categoryId = -1
                        viewModel.showLoading(true)
                        viewModel.setCatName(navArgs.categoryName)
                        viewModel.getAllProduct()
                    }

                    else -> {
                        categoryId = checkedIds[0]
                        viewModel.showLoading(true)
                        viewModel.setCatName(catMap[categoryId]!!)
                        viewModel.getProductByCat(checkedIds[0])
                    }

                }
            }
        }
    }


    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllData(categoryId)
        binding.chipCategoryFilter.check(navArgs.categoryId)
        sharedViewModel.reloadClickDone()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

}