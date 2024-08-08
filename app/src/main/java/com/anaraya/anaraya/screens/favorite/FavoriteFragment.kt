package com.anaraya.anaraya.screens.favorite

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
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentFavoriteBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.favorite.adapter.FavoriteAdapter
import com.anaraya.anaraya.screens.favorite.interaction.FavoriteInteraction
import com.anaraya.anaraya.screens.shared_data.ProductUiState
import com.anaraya.anaraya.util.plusQtyBasket
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment(), FavoriteInteraction {

    private val viewModel by viewModels<FavoriteViewModel>({ this })
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    //private var pos = -1
    private lateinit var list: MutableList<ProductUiState>
    private var selectedList: MutableList<Int> = mutableListOf()
    private var index = 0

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        adapter = FavoriteAdapter(this)
        binding.recyclerFav.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.navigateToHome) {
                    findNavController().navigate(FavoriteFragmentDirections.actionGlobalHomeFragment())
//                    findNavController().popBackStack()
                    viewModel.navigateToHomeDone()
                }
                if (it.deleteMsg != null) {
//                    Toast.makeText(context, it.deleteMsg, Toast.LENGTH_SHORT).show()
                    viewModel.clearDeleteMsg()
                }
                if (it.isSucceedAddProductToCart) {
                    if (index < selectedList.size) {
                        viewModel.addProductToBasket(selectedList[index])
                        index++
                    } else {
                        sharedViewModel.getCart()
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.selected_products_added_to_cart),
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.clearMsg()
                    }
                }
                if (it.addAllToBasketMsg != null) {
                    if (it.isSucceed) {
                        Toast.makeText(requireContext(), it.addAllToBasketMsg, Toast.LENGTH_SHORT)
                            .show()
                        sharedViewModel.getCart()
                        plusQtyBasket(
                            sharedPreferences,
                            sharedViewModel,
                            requireContext(),
                            it.numAdded
                        )
                    }else{
                        Toast.makeText(requireContext(),
                            getString(R.string.products_is_already_in_basket), Toast.LENGTH_SHORT)
                            .show()
                    }
                    viewModel.clearAddAll()
                }
                if (it.products.isNotEmpty()) {
                    list = it.products.toMutableList()
                    adapter.submitList(it.products)
                }
            }
        }

        binding.btnAddAllToCart.setOnClickListener {
            index = 0
            if (selectedList.isNotEmpty()) {
                viewModel.addProductToBasket(selectedList[index])
                index++
            } else {
                viewModel.addAllToBasket()
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
        showBottomNavBar(requireActivity(), false)
        showCardHome(requireActivity(), false)
        showToolBar(requireActivity(), true)
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        showToolBar(requireActivity(), true)
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    override fun onClickDelete(productId: Int, position: Int) {
        if(!viewModel.products.value.isLoading)
            viewModel.deleteFavProduct(productId, position, list)
    }

    override fun onClickCheckBox(productId: Int, isChecked: Boolean) {
        if (isChecked) {
            if (!selectedList.contains(productId))
                selectedList.add(productId)
        } else {
            if (selectedList.contains(productId))
                selectedList.remove(productId)
        }
        if (selectedList.size > 0)
            binding.btnAddAllToCart.text = getString(R.string.add_selected_to_cart)
        else
            binding.btnAddAllToCart.text = getString(R.string.add_all_to_cart)
    }
}