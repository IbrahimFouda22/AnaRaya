package com.anaraya.anaraya.screens.more.promo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import com.anaraya.anaraya.databinding.FragmentPromoCodeBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.more.promo.adapter.PromoCodeAdapter
import com.anaraya.anaraya.screens.more.promo.interaction.PromoInteraction
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PromoCodeFragment : Fragment(),PromoInteraction {

    private val viewModel by viewModels<PromoViewModel>({ this })
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private lateinit var binding: FragmentPromoCodeBinding
    private lateinit var adapter: PromoCodeAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPromoCodeBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        adapter = PromoCodeAdapter(this)
        binding.recyclerPromo.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.promoUiState.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.promoUiItem.isNotEmpty()) {
                    adapter.submitList(it.promoUiItem)
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
        showToolBar(requireActivity(),true)
        showBottomNavBar(requireActivity(),false)
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        showToolBar(requireActivity(), true)
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    override fun onClick(promoCode: String) {
        val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", promoCode)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, getString(R.string.promo_copied), Toast.LENGTH_SHORT).show()
    }

}