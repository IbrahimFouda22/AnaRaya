package com.anaraya.anaraya.screens.services.store.service.explore.service_details

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.anaraya.anaraya.util.copyText
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
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


        fromDate = getTodayDate(pattern = "yyyy-MM-dd HH:mm")
        toDate = getTodayDate(pattern = "yyyy-MM-dd HH:mm")
        binding.txtFromDateValue.text = getTodayDateLanguage(
            sharedPreferences, "MMM dd, yyyy hh:mm a"
        )
        binding.txtToDateValue.text = getTodayDateLanguage(
            sharedPreferences, "MMM dd, yyyy hh:mm a"
        )

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.product.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.product != null) {
                    if (it.product.rentFrom.isNotEmpty()) {
                        binding.txtFromDateValue.text = it.product.rentFrom
                        binding.txtToDateValue.text = it.product.rentTo
                        Log.d("TAG","av from : ${it.product.availableFromDate}")
                        Log.d("TAG","av to : ${it.product.availableToDate}")
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
        binding.txtSellerNumberValue.copyText(requireActivity(), requireContext())
        binding.txtFromDateValue.setOnClickListener {
            if (viewModel.product.value.product!!.rentFrom.isEmpty() && !viewModel.product.value.product!!.isUserService) {
                binding.txtFromDateValue.showSoftInputOnFocus = false
                val calendar = Calendar.getInstance()

                // Create and show the DatePickerDialog
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, year, monthOfYear, dayOfMonth ->
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, monthOfYear)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                        // Create and show the TimePickerDialog after selecting the date
                        val timePickerDialog = TimePickerDialog(
                            requireContext(),
                            { _, hourOfDay, minute ->
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                calendar.set(Calendar.MINUTE, minute)

                                // Combine selected date and time
                                val selectedDateTime = calendar.time

                                // Format the date and time in 24-hour format
                                val dateFormat24Hour = formatLanguageDate(
                                    date = selectedDateTime,
                                    sharedPreferences = sharedPreferences,
                                    pattern = "yyyy-MM-dd HH:mm"
                                )

                                // Format the date and time in 12-hour format with AM/PM
                                val dateFormat12Hour = formatLanguageDate(
                                    date = selectedDateTime,
                                    sharedPreferences = sharedPreferences,
                                    pattern = "MMM dd,yyyy hh:mm a"
                                )
                                // Display the formatted date and time
                                binding.txtFromDateValue.text = dateFormat12Hour

                                fromDate = dateFormat24Hour
                                sharedViewModel.setFromAndToDate(fromDate!!, toDate!!)

                                binding.txtFromDateValue.clearFocus()
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false // Set to false for 12-hour format with AM/PM
                        )
                        timePickerDialog.setCancelable(false)
                        timePickerDialog.show()

                        timePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                            binding.txtFromDateValue.clearFocus()
                            timePickerDialog.cancel()
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).apply {
                    setCancelable(false)

                    // Set min and max dates for DatePickerDialog
                    if (viewModel.product.value.product!!.availableFromDate != null) {
                        val minSelectableDate = Calendar.getInstance().apply {
                            time = viewModel.product.value.product!!.availableFromDate!!.toDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")!!
                            if(viewModel.product.value.product!!.numOfRented > 0)
                                add(Calendar.DAY_OF_MONTH, 1) // Add one day
                        }.time
                        datePicker.minDate = minSelectableDate.time
                    }

                    if (viewModel.product.value.product!!.availableToDate != null) {
                        val maxSelectableDate = Calendar.getInstance().apply {
                            time = viewModel.product.value.product!!.availableToDate!!.toDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")!!
                        }.time
                        datePicker.maxDate = maxSelectableDate.time
                    }
                }


                datePickerDialog.show()
                datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                    binding.txtFromDateValue.clearFocus()
                    datePickerDialog.cancel()
                }
            }
        }
        binding.txtToDateValue.setOnClickListener {
            if (viewModel.product.value.product!!.rentTo.isEmpty() && !viewModel.product.value.product!!.isUserService) {
                binding.txtToDateValue.showSoftInputOnFocus = false
                val calendar = Calendar.getInstance()

                // Create and show the DatePickerDialog
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, year, monthOfYear, dayOfMonth ->
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, monthOfYear)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                        // Create and show the TimePickerDialog after selecting the date
                        val timePickerDialog = TimePickerDialog(
                            requireContext(),
                            { _, hourOfDay, minute ->
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                calendar.set(Calendar.MINUTE, minute)

                                // Combine selected date and time
                                val selectedDateTime = calendar.time

                                // Format the date and time in 24-hour format
                                val dateFormat24Hour = formatLanguageDate(
                                    date = selectedDateTime,
                                    sharedPreferences = sharedPreferences,
                                    pattern = "yyyy-MM-dd HH:mm"
                                )

                                // Format the date and time in 12-hour format with AM/PM
                                val dateFormat12Hour = formatLanguageDate(
                                    date = selectedDateTime,
                                    sharedPreferences = sharedPreferences,
                                    pattern = "MMM dd,yyyy hh:mm a"
                                )
                                binding.txtToDateValue.text = dateFormat12Hour

                                toDate = dateFormat24Hour
                                sharedViewModel.setFromAndToDate(fromDate!!, toDate!!)

                                binding.txtToDateValue.clearFocus()
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false // Set to false for 12-hour format with AM/PM
                        )
                        timePickerDialog.setCancelable(false)
                        timePickerDialog.show()

                        timePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                            binding.txtToDateValue.clearFocus()
                            timePickerDialog.cancel()
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).apply {
                    setCancelable(false)

                    // Set min and max dates for DatePickerDialog
                    if (viewModel.product.value.product!!.availableFromDate != null) {
                        val minSelectableDate = Calendar.getInstance().apply {
                            time = viewModel.product.value.product!!.availableFromDate!!.toDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")!!
//                            add(Calendar.DAY_OF_MONTH, 1) // Add one day
                        }.time
                        datePicker.minDate = minSelectableDate.time
                    }

                    if (viewModel.product.value.product!!.availableToDate != null) {
                        val maxSelectableDate = Calendar.getInstance().apply {
                            time = viewModel.product.value.product!!.availableToDate!!.toDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")!!
                        }.time
                        datePicker.maxDate = maxSelectableDate.time
                    }
                }
                datePickerDialog.show()

                datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                    binding.txtToDateValue.clearFocus()
                    datePickerDialog.cancel()
                }
            }
        }
        binding.btnRequestToBuy.setOnClickListener {
            if (!viewModel.product.value.isLoading) {
                viewModel.requestToRent(
                    serviceId = viewModel.product.value.product!!.id,
                    from = if (viewModel.product.value.product!!.itIsARent) fromDate else null,
                    to = if (viewModel.product.value.product!!.itIsARent) toDate else null
                )
            }
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
    @SuppressLint("SimpleDateFormat")
    private fun String.toDate(pattern:String): Date? {
        val dateFormat = SimpleDateFormat(pattern)
        return dateFormat.parse(this)
    }
}