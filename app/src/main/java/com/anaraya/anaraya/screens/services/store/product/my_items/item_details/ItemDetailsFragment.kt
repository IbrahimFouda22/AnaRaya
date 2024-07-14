package com.anaraya.anaraya.screens.services.store.product.my_items.item_details

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
import com.anaraya.anaraya.databinding.FragmentItemDetailsBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ItemDetailsFragment : Fragment() {
    private lateinit var binding: FragmentItemDetailsBinding
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val navArgs by navArgs<ItemDetailsFragmentArgs>()
    @Inject
    lateinit var factory: ItemDetailsProductViewModel.ItemDetailsProductAssistedFactory
    private val viewModel by viewModels<ItemDetailsProductViewModel> {
        ItemDetailsProductViewModel.createItemDetailsProductFactory(factory, navArgs.productId)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentItemDetailsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        btnBack.setOnClickListener {
            reload()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.product.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.requestToDeleteMsg != null) {
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                    viewModel.resetMsg()
                    if (it.isSucceedRequestToDelete)
                        findNavController().popBackStack()
                }
            }
        }
        binding.btnRequestToDelete.setOnClickListener {
            viewModel.requestToDelete(sharedViewModel.homeState.value.itemDetailsProductId!!)
        }
        binding.btnRequestToEdit.setOnClickListener {
            findNavController().navigate(
                ItemDetailsFragmentDirections.actionItemDetailsFragmentToEditItemServiceFragment(
                    navArgs.productId
                )
            )
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