package com.anaraya.anaraya.screens.services.store.service.sell

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentSellServicesBinding
import com.anaraya.anaraya.databinding.LayoutDialogImageBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.services.store.product.sell.crateFileFromBitmap
import com.anaraya.anaraya.util.RealPathUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SellServicesFragment : Fragment() {
    private lateinit var binding: FragmentSellServicesBinding
    private val viewModel by viewModels<SellServicesViewModel>()
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private var categoryId: Int = -1
    private var subCategoryId: Int = -1
    private var isServiceRental: Boolean? = null
    private var fromDate: String? = null
    private var toDate: String? = null
    private var image: File? = null
    private lateinit var adapterServiceRental: ArrayAdapter<String>
    private lateinit var adapterCategories: ArrayAdapter<String>
    private lateinit var adapterSubCategories: ArrayAdapter<String>

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_allow_permission_to_upload_image),
                    Toast.LENGTH_SHORT
                ).show()
            else {
                pickImageFromGallery()
            }
        }
    private val requestPermissionCameraLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_allow_permission_to_take_an_image),
                    Toast.LENGTH_SHORT
                ).show()
            else {
                pickImageFromCamera()
            }
        }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSellServicesBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        fromDate = getTodayDate(pattern = "yyyy-MM-dd HH:mm")
        toDate = getTodayDate("yyyy-MM-dd HH:mm")
        binding.txtFromDateValue.text =
            if (sharedViewModel.homeState.value.fromDate == "") getTodayDateLanguage(
                sharedPreferences, "MMM dd, yyyy hh:mm a"
            ) else sharedViewModel.homeState.value.fromDateLang
        binding.txtToDateValue.text =
            if (sharedViewModel.homeState.value.toDate == "") getTodayDateLanguage(
                sharedPreferences,
                "MMM dd, yyyy hh:mm a"
            )
            else sharedViewModel.homeState.value.toDateLang
        isServiceRental = sharedViewModel.homeState.value.setVisibilityIsRental
        viewModel.visibilityServiceRental(isServiceRental ?: false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.sellUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.focusError) {
                    binding.root.clearFocus()
                    if (viewModel.sellUiState.value.titleError)
                        binding.txtTitleSellService.scroll()
                    else if (viewModel.sellUiState.value.categoryError)
                        binding.txtCategorySellService.scroll()
                    else if (viewModel.sellUiState.value.subCategoryError)
                        binding.txtSubCategorySellService.scroll()
                    else if (viewModel.sellUiState.value.isServiceRentalError)
                        binding.txtIsServiceRental.scroll()
                    else if (viewModel.sellUiState.value.descriptionError)
                        binding.txtItemDescriptionSellService.scroll()
                    else if (viewModel.sellUiState.value.priceError)
                        binding.txtPriceSellService.scroll()
                    else if (viewModel.sellUiState.value.locationError)
                        binding.txtLocationSellService.scroll()
                    else if (viewModel.sellUiState.value.pictureError)
                        binding.txtPictureSellService.scroll()
                    else
                        binding.checkboxAgreeSellStoreService.scroll()
                }
                if (it.addProductMsg != null) {
                    Toast.makeText(context, it.addProductMsg, Toast.LENGTH_SHORT).show()
                    viewModel.resetMsg()
                }
                if (it.sellCategoriesList.isNotEmpty() && adapterCategories.isEmpty) {
                    adapterCategories.addAll(it.sellCategoriesList.map { data -> data.name })
                    binding.edtCategorySellService.setAdapter(adapterCategories)
                }
                if (it.sellSubCategoriesList.isNotEmpty() && adapterSubCategories.isEmpty) {
                    val d = it.sellSubCategoriesList.map { data -> data.name }
                    adapterSubCategories.addAll(d)
                    binding.edtSubCategorySellService.setAdapter(adapterSubCategories)
                }
            }
        }
        binding.edtCategorySellService.setOnItemClickListener { _, _, position, _ ->
            if (categoryId != viewModel.sellUiState.value.sellCategoriesList[position].id) {
                binding.edtSubCategorySellService.text.clear()
                categoryId = viewModel.sellUiState.value.sellCategoriesList[position].id
                adapterSubCategories =
                    ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
                viewModel.getSubCategories(categoryId)
            }
        }
        binding.edtSubCategorySellService.setOnItemClickListener { _, _, position, _ ->
            if (subCategoryId != viewModel.sellUiState.value.sellSubCategoriesList[position].id) {
                subCategoryId = viewModel.sellUiState.value.sellSubCategoriesList[position].id
            }
        }
        binding.edtIsServiceRental.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    viewModel.visibilityServiceRental(true)
                    sharedViewModel.setVisibilityIsRental(true)
                    isServiceRental = true
                }

                1 -> {
                    viewModel.visibilityServiceRental(false)
                    sharedViewModel.setVisibilityIsRental(false)
                    isServiceRental = false
                }
            }
        }
        binding.checkboxAgreeSellStoreService.setOnCheckedChangeListener { _, isChecked ->
            if (sharedViewModel.homeState.value.isEnteredToTermsAndCondition) {
                if (viewModel.sellUiState.value.conditionAndTermsError) {
                    if (isChecked)
                        viewModel.updateStateTermsAndCondition()
                }
            } else {
                if (isChecked)
                    viewModel.updateStateTermsAndCondition()
                viewModel.resetErrors()
                sharedViewModel.setFromAndToDate(fromDate ?: "", toDate ?: "")
                sharedViewModel.setFromAndToDateLanguage(
                    binding.txtFromDateValue.text.toString(),
                    binding.txtToDateValue.text.toString()
                )
                sharedViewModel.setVisibilityIsRental(viewModel.sellUiState.value.isServiceRental)
                sharedViewModel.setTermsAndCondition(true)
                sharedViewModel.navigateToTermsAndCondition()
            }
        }
        binding.txtFromDateValue.setOnClickListener {
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
            )
            datePickerDialog.setCancelable(false)
            datePickerDialog.show()

            datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                binding.txtFromDateValue.clearFocus()
                datePickerDialog.cancel()
            }
        }
        binding.txtToDateValue.setOnClickListener {
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
            )
            datePickerDialog.setCancelable(false)
            datePickerDialog.show()

            datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                binding.txtToDateValue.clearFocus()
                datePickerDialog.cancel()
            }
        }
        binding.btnChoosePictureService.setOnClickListener {
            showDialog()
        }
        binding.btnSubmitSellStoreService.setOnClickListener {
            if (!viewModel.sellUiState.value.isLoading)
                addItem()
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
        viewModel.getAllData()
        binding.edtCategorySellService.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtSubCategorySellService.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtIsServiceRental.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        adapterServiceRental = ArrayAdapter(
            requireContext(),
            R.layout.layout_item_company_address
        )
        adapterServiceRental.addAll(resources.getStringArray(R.array.arrayServiceRental).toList())
        binding.edtIsServiceRental.setAdapter(adapterServiceRental)
        adapterCategories =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        adapterSubCategories =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        val view = LayoutDialogImageBinding.inflate(layoutInflater)
        dialog.setContentView(view.root)
        dialog.show()

        view.imgGallery.setOnClickListener {
            dialog.dismiss()
            checkExternalPermissionAndPickImage()
        }
        view.imgCamera.setOnClickListener {
            dialog.dismiss()
            checkExternalPermissionAndTakePhoto()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 3)
    }

    private fun pickImageFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 1)
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onActivityResult(requestCode, resultCode, data)",
            "androidx.fragment.app.Fragment"
        )
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 3) {
            val img = data?.data
            val file = File(RealPathUtil.getRealPath(requireContext(), img!!)!!)
            binding.edtPictureSellService.setText(file.name)
            image = file
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val img = data?.extras?.get("data")
            val file = crateFileFromBitmap(img as Bitmap, requireContext())
            binding.edtPictureSellService.setText(file.name)
            image = file
        }
    }

    private fun checkExternalPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            )
                pickImageFromGallery()
            else
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            )
                pickImageFromGallery()
            else
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun checkExternalPermissionAndTakePhoto() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
            pickImageFromCamera()
        else
            requestPermissionCameraLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun addItem() {
        viewModel.addItem(
            binding.edtTitleSell.text.toString(),
            categoryId,
            subCategoryId,
            binding.edtItemDescriptionSellService.text.toString(),
            binding.edtPriceSellService.text.toString(),
            binding.edtLocationSellService.text.toString(),
            image,
            binding.checkboxAgreeSellStoreService.isChecked,
            isServiceRental,
            sharedViewModel.homeState.value.fromDate,
            sharedViewModel.homeState.value.toDate
        )
    }

    private fun View.scroll() {
        binding.scrollSellStoreService.smoothScrollTo(0, this.top)
    }
}

fun getTodayDate(pattern: String = "yyyy-MM-dd"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("en"))
    return dateFormat.format(Date())
}

fun getTodayDateLanguage(
    sharedPreferences: SharedPreferences,
    pattern: String = "MMM dd, yyyy",
): String {
    return SimpleDateFormat(
        pattern,
        Locale(sharedPreferences.getString("lang", "en")!!)
    ).format(Date())
}

fun formatLanguageDate(
    date: Date,
    sharedPreferences: SharedPreferences,
    pattern: String = "MMM dd, yyyy",
): String {
    val dateFormat = SimpleDateFormat(
        pattern,
        Locale(sharedPreferences.getString("lang", "en")!!)
    )
    return dateFormat.format(date)
}
