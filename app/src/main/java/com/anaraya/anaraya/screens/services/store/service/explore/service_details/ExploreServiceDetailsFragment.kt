package com.anaraya.anaraya.screens.services.store.service.explore.service_details

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
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
import com.anaraya.anaraya.databinding.FragmentExploreServiceDetailsBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.services.store.service.sell.formatLanguageDate
import com.anaraya.anaraya.screens.services.store.service.sell.getTodayDate
import com.anaraya.anaraya.screens.services.store.service.sell.getTodayDateLanguage
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ExploreServiceDetailsFragment : Fragment() {
    private lateinit var binding: FragmentExploreServiceDetailsBinding
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private var fromDate: String? = null
    private var toDate: String? = null
    private val navArgs: ExploreServiceDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var factory: ExploreServiceDetailsViewModel.ItemDetailsServiceAssistedFactory
    private val viewModel by viewModels<ExploreServiceDetailsViewModel> {
        ExploreServiceDetailsViewModel.createItemDetailsServiceFactory(factory, navArgs.serviceId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExploreServiceDetailsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        fromDate = getTodayDate()
        toDate = getTodayDate()
        binding.txtFromDateValue.text = getTodayDateLanguage(sharedPreferences)
        binding.txtToDateValue.text = getTodayDateLanguage(sharedPreferences)

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.product.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.product != null) {
                    if (it.product.rentFrom.isNotEmpty()) {
                        binding.txtFromDateValue.text = it.product.rentFrom
                        binding.txtToDateValue.text = it.product.rentTo
                    }
                }
                if (!it.requestToBuyMessage.isNullOrEmpty()) {
                    Toast.makeText(context, it.requestToBuyMessage, Toast.LENGTH_SHORT).show()
                    if (it.isSucceedRequestToBuy) {
                        viewModel.getStoreServiceByIdForCustomer()
                    }
                }
            }
        }
        binding.txtFromDateValue.setOnClickListener {
            if (viewModel.product.value.product!!.rentFrom.isEmpty() && !viewModel.product.value.product!!.isUserService) {
                binding.txtFromDateValue.showSoftInputOnFocus = false
                val d = DatePickerDialog(requireContext())
                d.setCancelable(false)
                d.show()
                d.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    val day =
                        if (d.datePicker.dayOfMonth.toString().length == 1) "0${d.datePicker.dayOfMonth}" else d.datePicker.dayOfMonth
                    var m = d.datePicker.month.toString().toInt()
                    m++
                    val month =
                        if (m.toString().length == 1) "0${m}" else m
//                val monthName = monthNames[m - 1]
                    fromDate = "${d.datePicker.year}-$month-$day"
                    binding.txtFromDateValue.text =
                        formatLanguageDate(
                            d.datePicker.year,
                            m,
                            day.toString().toInt(),
                            sharedPreferences
                        )
                    sharedViewModel.setFromAndToDate(fromDate!!, toDate!!)
                    binding.txtFromDateValue.clearFocus()
                    d.cancel()
                }
                d.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                    binding.txtFromDateValue.clearFocus()
                    d.cancel()
                }
            }
        }
        binding.txtToDateValue.setOnClickListener {
            if (viewModel.product.value.product!!.rentTo.isEmpty() && !viewModel.product.value.product!!.isUserService) {
                binding.txtToDateValue.showSoftInputOnFocus = false
                val d = DatePickerDialog(requireContext())
                d.setCancelable(false)
                d.show()
                d.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    val day =
                        if (d.datePicker.dayOfMonth.toString().length == 1) "0${d.datePicker.dayOfMonth}" else d.datePicker.dayOfMonth
                    var m = d.datePicker.month.toString().toInt()
                    m++
                    val month =
                        if (m.toString().length == 1) "0${m}" else m
//                val monthName = monthNames[m - 1]
                    toDate = "${d.datePicker.year}-$month-$day"
                    binding.txtToDateValue.text =
                        formatLanguageDate(
                            d.datePicker.year,
                            m,
                            day.toString().toInt(),
                            sharedPreferences
                        )
                    sharedViewModel.setFromAndToDate(fromDate!!, toDate!!)
                    binding.txtToDateValue.clearFocus()
                    d.cancel()
                }
                d.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                    binding.txtToDateValue.clearFocus()
                    d.cancel()
                }
            }
        }
        binding.btnRequestToBuy.setOnClickListener {
            viewModel.requestToRent(
                serviceId = viewModel.product.value.product!!.id,
                from = if (viewModel.product.value.product!!.itIsARent) fromDate else null,
                to = if (viewModel.product.value.product!!.itIsARent) toDate else null
            )
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