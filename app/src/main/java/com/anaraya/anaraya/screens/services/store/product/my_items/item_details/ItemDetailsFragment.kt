package com.anaraya.anaraya.screens.services.store.product.my_items.item_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentItemDetailsBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.services.store.adapter.CompanyAddressSelectedAdapter
import com.anaraya.anaraya.screens.services.store.interaction.AddressesInteraction
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ItemDetailsFragment : Fragment(), AddressesInteraction {
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
    private var selectedAddress: String? = null
    private var allCompaniesId = -1
    private var allCompanies = emptyList<Int>()

    private var allGovernorateId = -1
    private var allGovernorate = emptyList<String>()

    private var allCompanyAddressId = -1
    private var allCompanyAddress = emptyList<String>()
    private val adapter by lazy { CompanyAddressSelectedAdapter(this) }
    private var selectedPos = -1
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
        binding.edtCompanyDelivery.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtLineGovernmentDelivery.setDropDownBackgroundResource(R.drawable.shape_drop_down)

        val adapterAllCompanies =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        binding.edtCompanyDelivery.setAdapter(adapterAllCompanies)
        binding.recyclerAddress.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.product.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.requestToDeleteMsg != null) {
                    if (it.isSucceedRequestToDelete)
                        findNavController().popBackStack()
                    else
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                    viewModel.resetMsg()
                }
                if (it.isSucceedConfirmDeal) {
                    findNavController().popBackStack()
                }
                if (it.allCompanies.isNotEmpty()) {
                    if (allCompanies.isEmpty()) {
                        allCompanies = it.allCompanies.map { data ->
                            data.id
                        }
                        val m = it.allCompanies.map { data ->
                            data.name
                        }
                        adapterAllCompanies.addAll(m)
                    }
                }
                if (it.allGovernorate.isNotEmpty()) {
                    if (allGovernorate.isEmpty()) {
                        allGovernorate = it.allGovernorate
                        val adapterAllGovernorate =
                            ArrayAdapter<String>(
                                requireContext(),
                                R.layout.layout_item_company_address
                            )
                        binding.edtLineGovernmentDelivery.setAdapter(adapterAllGovernorate)
                        adapterAllGovernorate.addAll(it.allGovernorate)
                    }
                }
                if (it.allGovernorate.isEmpty()) {
                    val adapterAllGovernorate =
                        ArrayAdapter<String>(
                            requireContext(),
                            R.layout.layout_item_company_address
                        )
                    binding.edtLineGovernmentDelivery.setAdapter(adapterAllGovernorate)

                }
                if (it.allCompanyAddresses != null) {
                    it.allCompanyAddresses.collectLatest { data ->
                        adapter.submitData(data)
                    }
                }
                if (it.allCompanyAddresses == null) {
                    adapter.submitData(PagingData.empty())
                }
            }
        }

        binding.btnRequestToDelete.setOnClickListener {
            viewModel.requestToDelete(navArgs.productId)
        }
        binding.btnRequestToEdit.setOnClickListener {
            findNavController().navigate(
                ItemDetailsFragmentDirections.actionItemDetailsFragmentToEditItemServiceFragment(
                    navArgs.productId
                )
            )
        }
        binding.txtPaymentMethod.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.menu.add(getString(R.string.cash))
            popupMenu.menu.add(getString(R.string.in_person_card_payment))
            popupMenu.menu.add(getString(R.string.bank_transfer))
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener { item ->
                viewModel.setCashNum(
                    if (item.title.toString() == getString(R.string.cash)) 1 else if (item.title.toString() == getString(
                            R.string.in_person_card_payment
                        )
                    ) 2 else 3
                )
                viewModel.setSelectedMethod(item.title.toString())
                true
            }
        }
        binding.btnSubmit.setOnClickListener {
            checkSelectedMethod()
        }
        binding.btnProceedWithSale.setOnClickListener {
            viewModel.product.value.product?.let { it1 ->
                viewModel.proceedWithSale(
                    it1.customerWantsToBuy
                )
            }
        }

        binding.btnOrderComplete.setOnClickListener {
            viewModel.orderComplete()
        }
        binding.edtCompanyDelivery.setOnItemClickListener { _, _, position, _ ->
            if (allCompaniesId != position) {
                selectedAddress = null
                allCompaniesId = position

                allGovernorate = emptyList()
                allGovernorateId = -1

                allCompanyAddress = emptyList()
                allCompanyAddressId = -1
                binding.edtLineGovernmentDelivery.text.clear()

                viewModel.getAllGovernorateByCompanyId(allCompanies[position])
            }
        }
        binding.edtLineGovernmentDelivery.setOnItemClickListener { _, _, position, _ ->
            if (allGovernorateId != position) {
                selectedAddress = null
                allGovernorateId = position

                allCompanyAddress = emptyList()
                allCompanyAddressId = -1

                viewModel.getAllCompanyAddresses(
                    allCompanies[allCompaniesId],
                    allGovernorate[position]
                )
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

    private fun checkSelectedMethod() {
        if (viewModel.product.value.selectedMethod == null) {
            val anim =
                AnimationUtils.loadAnimation(requireContext(), R.anim.shake_animation)
            anim.duration = 100
            anim.repeatMode = 2
            anim.repeatCount = 50
            binding.txtPaymentMethod.startAnimation(anim)
        } else {
            //after selected method
            viewModel.confirmDeal(
                companyAddress = selectedAddress,
                company = if (allCompaniesId > -1) "s" else null,
                governorate = if (allGovernorateId > -1) "s" else null,
                listeningId = viewModel.product.value.product!!.customerInformation!!.listeningId,
                paymentMethod = viewModel.product.value.cashStatus
            )
        }
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

    override fun onClick(id: String, position: Int) {
        selectedAddress = id
        if(selectedPos != -1)
            adapter.changeSelected(selectedPos, false)
        selectedPos = position
        adapter.changeSelected(position, true)
    }

}