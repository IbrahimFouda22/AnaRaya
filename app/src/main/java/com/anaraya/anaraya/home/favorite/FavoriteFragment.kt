package com.anaraya.anaraya.home.favorite

import android.content.SharedPreferences
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
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentFavoriteBinding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.anaraya.anaraya.home.favorite.adapter.FavoriteAdapter
import com.anaraya.anaraya.home.favorite.interaction.FavoriteInteraction
import com.anaraya.anaraya.home.shared_data.ProductUiState
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
    private lateinit var list :MutableList<ProductUiState>
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.navigateToHome) {
                    findNavController().navigate(FavoriteFragmentDirections.actionGlobalHomeFragment())
//                    findNavController().popBackStack()
                    viewModel.navigateToHomeDone()
                }
                if (it.deleteMsg != null) {
                    Toast.makeText(context, it.deleteMsg, Toast.LENGTH_SHORT).show()
                    viewModel.clearDeleteMsg()
                }
                if (it.addAllToBasketMsg != null) {
                    Toast.makeText(context, it.addAllToBasketMsg, Toast.LENGTH_SHORT).show()
                    plusQtyBasket(sharedPreferences,sharedViewModel,requireContext(),it.numAdded)
                    viewModel.clearAddAll()
                }
                if (it.products.isNotEmpty()) {
                    list = it.products.toMutableList()
                    adapter.submitList(it.products)
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
        viewModel.deleteFavProduct(productId,position,list)
    }
}