package com.anaraya.anaraya.screens.cart

import android.content.SharedPreferences
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentCartBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.cart.adapter.CartAdapter
import com.anaraya.anaraya.screens.cart.adapter.CartAddressAdapter
import com.anaraya.anaraya.screens.cart.interaction.CartAddressInteraction
import com.anaraya.anaraya.screens.cart.interaction.CartInteraction
import com.anaraya.anaraya.util.gone
import com.anaraya.anaraya.util.minusNumBasket
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import com.anaraya.anaraya.util.showToolBar
import com.anaraya.anaraya.util.visible
import com.google.android.material.card.MaterialCardView
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment(), CartInteraction, CartAddressInteraction {
    private val viewModel by viewModels<CartViewModel>({ this })
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var bottomNav: ChipNavigationBar
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private lateinit var binding: FragmentCartBinding
    private lateinit var adapter: CartAdapter
    private lateinit var addressesAdapter: CartAddressAdapter
    private var isExpanded = false
    private var checkOutUiState: CheckOutUiStateData? = null

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.selectedMethod = viewModel.cartUiState.value.selectedMethod
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        bottomNav = requireActivity().findViewById(R.id.bottomNavHome)
        adapter = CartAdapter(this)
        binding.recyclerCart.adapter = adapter
        addressesAdapter = CartAddressAdapter(this)
        binding.recyclerAddressesCart.adapter = addressesAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartUiState.collectLatest {
                if (it.error != null) {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.errorChangeDefaultAddress != null) {
                    viewModel.getAddresses()
                    viewModel.resetMsg()
                }
                if (it.changeAddressUiState != null) {
                    viewModel.getAddresses()
                    viewModel.resetMsg()
                }
                if (it.navigateToMarket) {
                    bottomNav.setItemSelected(R.id.homeFragment)
                    findNavController().navigate(CartFragmentDirections.actionGlobalHomeFragment())
                    viewModel.navigateToMarketDone()
                }
                if (it.navigateToSchedule) {
                    findNavController().navigate(CartFragmentDirections.actionCartFragmentToScheduleFragment())
                    viewModel.navigateToScheduleDone()
                }
                if (it.navigateToTotalCost) {
                    findNavController().navigate(
                        CartFragmentDirections.actionCartFragmentToTotalCostFragment(
                            it.addOrderUiState!!,
                            viewModel.cartUiState.value.selectedMethod!!,
                            it.totalPoints.toFloat()
                        )
                    )
                    viewModel.navigateToTotalCostDone()
                }
                if (it.deleteMsg != null) {
                    //Toast.makeText(context, it.deleteMsg, Toast.LENGTH_SHORT).show()
                    if (it.isSucceedDeleteProduct) {
                        minusNumBasket(sharedPreferences, sharedViewModel, requireContext())
//                        if(it.isPoints)
//                            sharedViewModel.getPoints()
                        if (!it.isPoints)
                            sharedViewModel.getTrending()
                    }
                }
                if (it.checkOutUiState != null) {
                    checkOutUiState = it.checkOutUiState
                }
                if (it.cartUiState != null) {
                    adapter.submitList(it.cartUiState.cartUiListState)
                }
                if (it.allAddressesUiState.isNotEmpty()) {
                    if (!isExpanded) {
                        binding.txtSelectedAddress.visible()
                        binding.txtSelectedAddressValue.visible()
                        binding.recyclerAddressesCart.gone()
                        binding.txtAddAddressCart.gone()
                    }
                    addressesAdapter.submitList(it.allAddressesUiState)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigateToAddAddress.collectLatest {
                if (it) {
                    sharedViewModel.setSelectedMethodInCart(viewModel.cartUiState.value.selectedMethod)
                    sharedViewModel.saveSelectedMethodInCart(true)
                    findNavController().navigate(
                        CartFragmentDirections.actionCartFragmentToAddAddressFragment(
                            true
                        )
                    )
                    viewModel.navigateToAddAddressDone()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.homeState.collectLatest {
                if (it.getCart) {
                    viewModel.getAllData()
                    sharedViewModel.getCartDone()
                }
            }
        }
        binding.txtDeliveryAddressCart.setOnClickListener {
            var v = binding.txtSelectedAddress.expand(binding.cardAddAddressCart)
            binding.txtSelectedAddress.visibility = v
            v = binding.txtSelectedAddressValue.expand(binding.cardAddAddressCart)
            binding.txtSelectedAddressValue.visibility = v
            v = binding.txtAddAddressCart.expand(binding.cardAddAddressCart)
            binding.txtAddAddressCart.visibility = v
            v = binding.recyclerAddressesCart.expand(binding.cardAddAddressCart)
            binding.recyclerAddressesCart.visibility = v
        }
//        binding.txtDeliveryAddressCart.setOnClickListener {
//            isExpanded = !isExpanded
//            if (isExpanded) {
//                binding.txtSelectedAddress.gone()
//                binding.txtSelectedAddressValue.gone()
//                binding.recyclerAddressesCart.visible()
//                binding.txtAddAddressCart.visible()
//            } else {
//                binding.txtSelectedAddress.visible()
//                binding.txtSelectedAddressValue.visible()
//                binding.recyclerAddressesCart.gone()
//                binding.txtAddAddressCart.gone()
//            }
//        }
        binding.btnGoToMarget.setOnClickListener {
            viewModel.navigateToMarket()
        }
        binding.btnAddItems.setOnClickListener {
            viewModel.navigateToMarket()
        }
        binding.btnCheckOut.setOnClickListener {
            if (viewModel.cartUiState.value.cartUiState != null) {
                if (viewModel.cartUiState.value.cartUiState!!.hasAddress) {
                    if (viewModel.cartUiState.value.cartUiState!!.cartUiListState.isNotEmpty()) {
                        checkSelectedMethod()
                    } else
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.please_add_items), Toast.LENGTH_SHORT
                        ).show()
                } else {
                    //fromCart = false
                    viewModel.navigateToAddAddress()
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.try_later), Toast.LENGTH_LONG)
                    .show()
            }
        }
        binding.txtAddAddressCart.setOnClickListener {
            viewModel.navigateToAddAddress()
        }
        binding.txtPaymentCartMethod.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            var counter = 100
            checkOutUiState?.let {
                for ((order, item) in checkOutUiState!!.paymentMethods.withIndex()) {
                    popupMenu.menu.add(R.id.paymentGroup, counter, order, item)
                    counter++
                }
                popupMenu.show()
                popupMenu.setOnMenuItemClickListener { item ->
                    viewModel.setSelectedMethod(item.title.toString())
                    binding.selectedMethod = viewModel.cartUiState.value.selectedMethod
                    true
                }
            }
        }

        if (sharedViewModel.homeState.value.saveSelected) {
            sharedViewModel.homeState.value.selectedMethodInCart?.let {
                viewModel.setSelectedMethod(
                    it
                )
                binding.selectedMethod = it
            }
            sharedViewModel.saveSelectedMethodInCart(false)
        }
        btnBack.setOnClickListener {
            reload()
        }
        btnReload.setOnClickListener {
            reload()
        }
        return binding.root
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    override fun onStart() {
        super.onStart()
        showBottomNavBar(requireActivity(), false)
        showCardHome(requireActivity(), false)
        showToolBar(requireActivity(), false)
    }

    override fun onClickDelete(productId: Int, position: Int, isLoyalty: Boolean) {
        viewModel.removeProduct(productId, isLoyalty)
    }

    override fun onClickPlus(productId: Int, newQty: Int, isLoyalty: Boolean) {
        viewModel.addProductToCart(productId, newQty, isLoyalty)
    }

    override fun onClickMinus(productId: Int, newQty: Int, isLoyalty: Boolean) {
        viewModel.addProductToCart(productId, newQty, isLoyalty)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    private fun checkSelectedMethod() {
        if (viewModel.cartUiState.value.selectedMethod == null) {
            val anim =
                AnimationUtils.loadAnimation(requireContext(), R.anim.shake_animation)
            anim.duration = 100
            anim.repeatMode = 2
            anim.repeatCount = 50
            binding.txtPaymentCartMethod.startAnimation(anim)
        } else {
            sharedViewModel.setCartList(viewModel.cartUiState.value.cartUiState!!.cartUiListState)
            viewModel.navigateToTotalCost()
        }
    }

    override fun onClickAddress(id: String, position: Int, isUserAddress: Boolean) {
        viewModel.changeDefaultAddress(id, isUserAddress)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.getCart()

    }

    private fun View.expand(card: MaterialCardView): Int {
        val v = if (this.visibility == View.GONE) View.VISIBLE else View.GONE
        TransitionManager.beginDelayedTransition(card, AutoTransition())
        return v
    }
}