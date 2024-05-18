package com.anaraya.anaraya.home.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentMarketBinding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.anaraya.anaraya.home.category.adapter.CategoryAdapter
import com.anaraya.anaraya.home.shared_interaction.CategoryInteraction
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showButtonCart
import com.anaraya.anaraya.util.showButtonFav
import com.anaraya.anaraya.util.showButtonFilter
import com.anaraya.anaraya.util.showCardHome
import com.anaraya.anaraya.util.showTextSchedule
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MarketFragment : Fragment(), CategoryInteraction {

    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private lateinit var btnCart: ImageButton
    private val viewModel by viewModels<CategoryViewModel>({ this })
    private lateinit var edtSearch: EditText
    private lateinit var binding: FragmentMarketBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(layoutInflater)
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        btnCart = requireActivity().findViewById(R.id.btnCart)

        val adapter = CategoryAdapter(this)
        binding.recyclerCategoryMarket.adapter = adapter
        edtSearch = requireActivity().findViewById(R.id.edtSearch)


        lifecycleScope.launch {
            viewModel.categories.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.navigateToSearch){
//                    findNavController().navigate(MarketFragmentDirections.actionMarketFragmentToSearchFragment())
//                    viewModel.navigateToSearchDone()
                }
                if (it.categoryUiStateData != null) {
                    it.categoryUiStateData.collect { data ->
                        adapter.submitData(data)
                    }
                }

            }
        }

        lifecycleScope.launch {
            sharedViewModel.homeState.collectLatest {
                if (it.navigateToFav) {
//                    findNavController().navigate(MarketFragmentDirections.actionMarketFragmentToFavoriteFragment())
//                    sharedViewModel.navigateToFavDone()
                }
            }
        }
        lifecycleScope.launch {
            sharedViewModel.navigateToCart.collectLatest {
                if (it) {
//                    findNavController().navigate(MarketFragmentDirections.actionMarketFragmentToCartFragment())
//                    sharedViewModel.navigateToCartDone()
                }
            }
        }
        edtSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.navigateToSearch()
            }
        }

        btnBack.setOnClickListener {
            reload()
        }
        btnReload.setOnClickListener {
            reload()
        }
        btnCart.setOnClickListener {
            sharedViewModel.navigateToCart()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        showToolBar(requireActivity(), false)
        showTextSchedule(requireActivity(), false)
        showCardHome(requireActivity(), true)
        showBottomNavBar(requireActivity(), true)
        showButtonFilter(requireActivity(), false)
        showButtonFav(requireActivity(), true)
        showButtonCart(requireActivity(), true)
    }

    override fun onClickItem(categoryId: Int, categoryName: String) {
//        findNavController().navigate(
//            MarketFragmentDirections.actionMarketFragmentToCategoryProductFragment(
//                categoryName,
//                categoryId
//            )
//        )
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllDate()
        sharedViewModel.reloadClickDone()
    }
}