package com.anaraya.anaraya.screens.services.store.service.my_items.item_sold

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentServiceSoldBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.services.store.adapter.ServiceHistoryAdapter
import com.anaraya.anaraya.screens.services.store.interaction.NumberInteraction
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ServiceSoldFragment : Fragment(), NumberInteraction {
    private lateinit var binding: FragmentServiceSoldBinding
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val navArgs by navArgs<ServiceSoldFragmentArgs>()

    @Inject
    lateinit var factory: ServiceSoldViewModel.ServiceSoldAssistedFactory
    private val viewModel by viewModels<ServiceSoldViewModel> {
        ServiceSoldViewModel.createServiceSoldFactory(factory, navArgs.serviceId)
    }
    private lateinit var adapter: ServiceHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentServiceSoldBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        adapter = ServiceHistoryAdapter(this)
        binding.recyclerList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.product.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.product != null) {
                    if (it.product.customerInformation.isNotEmpty()) {
                        adapter.submitList(
                            it.product.customerInformation.filter { state ->
                                state.sellingStatus == 5 || state.sellingStatus == 6
                            }
                        )
                    }
                }
            }
        }
        binding.txtServiceHistory.setOnClickListener {
            val v = binding.recyclerList.expand(binding.cardList)
            binding.recyclerList.visibility = v
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
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    private fun View.expand(card: MaterialCardView): Int {
        val v = if (this.visibility == View.GONE) View.VISIBLE else View.GONE
        TransitionManager.beginDelayedTransition(card, AutoTransition())
        return v
    }

    override fun onClickNumberValue(text: String) {
        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.copied_text), text)
        clipboard.setPrimaryClip(clip)
        // Optionally show a toast or other feedback to the user
        Toast.makeText(
            requireContext(),
            getString(R.string.text_copied_to_clipboard), Toast.LENGTH_SHORT
        ).show()
    }
}