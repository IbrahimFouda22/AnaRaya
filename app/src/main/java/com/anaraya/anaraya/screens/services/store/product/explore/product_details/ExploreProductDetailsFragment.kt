package com.anaraya.anaraya.screens.services.store.product.explore.product_details


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
import com.anaraya.anaraya.databinding.FragmentExploreProductDetailsBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.copyText
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ExploreProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentExploreProductDetailsBinding
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val navArgs: ExploreProductDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ExploreProductDetailsViewModel.ItemDetailsProductAssistedFactory
    private val viewModel by viewModels<ExploreProductDetailsViewModel> {
        ExploreProductDetailsViewModel.createItemDetailsProductFactory(factory, navArgs.productId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExploreProductDetailsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.product.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (!it.requestToBuyMessage.isNullOrEmpty()) {
                    Toast.makeText(context, it.requestToBuyMessage, Toast.LENGTH_SHORT).show()
                    if (it.isSucceedRequestToBuy) {
                        viewModel.getStoreProductByIdForOwner()
                    }
                }
            }
        }
        binding.txtSellerNumberValue.copyText(requireActivity(),requireContext())
        binding.btnRequestToBuy.setOnClickListener {
            if(!viewModel.product.value.isLoading)
                viewModel.requestToBuy(viewModel.product.value.product!!.id)
        }
        binding.btnBackAllExploreProductDetails.setOnClickListener {
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
        sharedViewModel.reloadClickDone()
    }
}