package com.anaraya.anaraya.screens.total_cost

import android.animation.LayoutTransition
import android.content.SharedPreferences
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
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
import com.anaraya.anaraya.databinding.FragmentTotalCostBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.total_cost.adapter.CheckOutAdapter
import com.anaraya.anaraya.util.resetQtyBasket
import com.anaraya.anaraya.util.visible
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class TotalCostFragment : Fragment() {

    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private val navArgs by navArgs<TotalCostFragmentArgs>()
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private lateinit var binding: FragmentTotalCostBinding
    private lateinit var toolBar: MaterialToolbar
    private lateinit var bottomNav: ChipNavigationBar
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var factory: TotalCostViewModel.TotalCostAssistedFactory
    private val viewModel by viewModels<TotalCostViewModel> {
        TotalCostViewModel.createTotalCostFactory(factory, navArgs.orderState,navArgs.totalPoints.toDouble())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTotalCostBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        toolBar = requireActivity().findViewById(R.id.toolBarActivity)
        bottomNav = requireActivity().findViewById(R.id.bottomNavHome)

        toolBar.setNavigationIcon(R.drawable.ic_nav_back)
        toolBar.visible()



        val adapter = CheckOutAdapter()
        adapter.submitList(sharedViewModel.homeState.value.cartUiListState)
        binding.recyclerCheckOut.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalCostUiState.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.navigateToMarket) {
                    bottomNav.setItemSelected(R.id.homeFragment)
                    findNavController().navigate(TotalCostFragmentDirections.actionGlobalHomeFragment())
                    viewModel.navigateToMarketDone()
                }
                if (it.navigateToOrder) {
                    findNavController().navigate(TotalCostFragmentDirections.actionTotalCostFragmentToOrderFragment())
                    viewModel.navigateToOrderDone()
                }
                if (it.message != null) {
                    resetQtyBasket(sharedPreferences,sharedViewModel,requireContext())
//                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetMsg()
                }
                if (it.messageApplyPromo != null) {
//                    Toast.makeText(context, it.messageApplyPromo, Toast.LENGTH_SHORT).show()
                    viewModel.resetMsg()
                }
                if (it.isSucceed) {
                    viewModel.navigateToOrder()
                }
            }
        }
        binding.txtOrderReviewTotal.setOnClickListener {
            val v = binding.recyclerCheckOut.expand(binding.cardOrderReview)
            binding.recyclerCheckOut.visibility = v
        }
        binding.btnPlaceOrderTotalCost.setOnClickListener {
            if(!viewModel.totalCostUiState.value.isLoading)
                viewModel.placeOrder(navArgs.paymentMethod)
        }
        binding.txtCoupon.setOnClickListener {
            val v = binding.edtCoupon.expand(binding.cardCoupon)
            binding.edtCoupon.visibility = v
            binding.btnApplyCoupon.visibility = v
        }
        binding.btnApplyCoupon.setOnClickListener {
            if(!viewModel.totalCostUiState.value.isLoading) {
                if(viewModel.totalCostUiState.value.addOrderUiState!!.discount > 0)
                    viewModel.removePromo()
                else{
                    if (!binding.edtCoupon.text.isNullOrEmpty())
                        viewModel.applyPromo(binding.edtCoupon.text.toString())
                }
            }
        }
        binding.cardCoupon.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        return binding.root
    }

    private fun View.expand(card: MaterialCardView): Int {
        val v = if (this.visibility == View.GONE) View.VISIBLE else View.GONE
        TransitionManager.beginDelayedTransition(card, AutoTransition())
        return v
    }

    override fun onStart() {
        super.onStart()
        if(navArgs.orderState.discount > 0){
            binding.edtCoupon.setText(navArgs.orderState.promoCode)
            binding.btnApplyCoupon
        }
    }

}