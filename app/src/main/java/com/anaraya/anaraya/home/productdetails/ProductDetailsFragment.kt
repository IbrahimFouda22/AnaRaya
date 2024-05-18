package com.anaraya.anaraya.home.productdetails

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
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentProductDetailsBinding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.anaraya.anaraya.home.shared_interaction.ProductInteractionListener
import com.anaraya.anaraya.util.minusNumBasket
import com.anaraya.anaraya.util.plusNumBasket
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(), ProductInteractionListener {

    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private lateinit var binding: FragmentProductDetailsBinding
    private val navArgs by navArgs<ProductDetailsFragmentArgs>()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var factory: ProductDetailsViewModel.AssistedFactory
    private val viewModel by viewModels<ProductDetailsViewModel> {
        ProductDetailsViewModel.createFactory(navArgs.productId, factory)
    }

    private var pos = -1
    private var productID = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.sharedViewModel = sharedViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        val adapter = ProductDetailsAdapter()
        binding.viewPagerProductDetails.adapter = adapter
        binding.dotsIndicatorSliderProductDetails.attachTo(binding.viewPagerProductDetails)

        val recommendedAdapter = ProductDetailsRecommendedAdapter(this)
        binding.recyclerRecommended.adapter = recommendedAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.product.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (!it.messageAdd.isNullOrEmpty()) {
                    //Toast.makeText(context, it.messageAdd, Toast.LENGTH_SHORT).show()
                    plusNumBasket(sharedPreferences, sharedViewModel, requireContext())
                    viewModel.resetAddCartMessage()
                }
                if (!it.messageDelete.isNullOrEmpty()) {
                    //Toast.makeText(context, it.messageDelete, Toast.LENGTH_SHORT).show()
                    minusNumBasket(sharedPreferences, sharedViewModel, requireContext())
                    viewModel.resetRemoveCartMessage()
                }
                if (!it.messageAddDeleteFav.isNullOrEmpty()) {
                    //Toast.makeText(context, it.messageAddDeleteFav, Toast.LENGTH_SHORT).show()
                    viewModel.resetAddFavMessage()
                }
                if (!it.messageAddRecommended.isNullOrEmpty()) {
                    //Toast.makeText(context, it.messageAddRecommended, Toast.LENGTH_SHORT).show()
                    recommendedAdapter.changeIcon(pos)
                    plusNumBasket(sharedPreferences, sharedViewModel, requireContext())
                    pos = -1
                    viewModel.resetAddRecommendedCartMessage()
                }
                if (it.navigateToCart) {
                    findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToCartFragment())
                    viewModel.navigateToCartDone()
                }
                if (it.navigateToProductDetails) {
                    findNavController().navigate(
                        ProductDetailsFragmentDirections.actionProductDetailsFragmentSelf(
                            productID
                        )
                    )
                    viewModel.navigateToProductDetailsDone()
                }
                if (it.recommendedList.isNotEmpty()) {
                    recommendedAdapter.submitList(it.recommendedList)
                }
                if (it.productDetailsUiState != null) {
                    adapter.submitList(it.productDetailsUiState.images)
                }
            }
        }
        binding.btnAddToBasket.setOnClickListener {
            if (viewModel.product.value.qtyInBasket == 0)
                Toast.makeText(context, getString(R.string.please_add_qty), Toast.LENGTH_SHORT)
                    .show()
            else
                viewModel.addProductToCart(viewModel.product.value.qtyInBasket,false)
        }
//        binding.btnFavProductDetails.setOnClickListener {
//            if(viewModel.product.value.productDetailsUiState.favouriteStock)
//        }


        binding.btnBackProductDetails.setOnClickListener {
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

    override fun onStart() {
        super.onStart()
        showCardHome(requireActivity(), false)
        showBottomNavBar(requireActivity(), false)
    }


    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllDate()
        sharedViewModel.reloadClickDone()
    }

    override fun onCLick(productId: Int) {
        productID = productId
        viewModel.navigateToProductDetails()
    }

    override fun addToCart(productId: Int, position: Int) {
        viewModel.addProductRecommendedToCart(productId, 1)
        pos = position
    }

}