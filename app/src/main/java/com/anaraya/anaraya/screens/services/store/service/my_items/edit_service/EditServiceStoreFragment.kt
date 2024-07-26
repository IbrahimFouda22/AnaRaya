package com.anaraya.anaraya.screens.services.store.service.my_items.edit_service

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
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
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentEditServiceStoreBinding
import com.anaraya.anaraya.databinding.LayoutDialogImageBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.services.store.product.sell.crateFileFromBitmap
import com.anaraya.anaraya.screens.services.store.service.sell.formatLanguageDate
import com.anaraya.anaraya.screens.services.store.service.sell.getTodayDate
import com.anaraya.anaraya.screens.services.store.service.sell.getTodayDateLanguage
import com.anaraya.anaraya.util.RealPathUtil
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@Suppress("DEPRECATION")
@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class EditServiceStoreFragment : Fragment() {
    private lateinit var binding: FragmentEditServiceStoreBinding
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private var categoryId: Int = -1
    private var subCategoryId: Int = -1
    private var isRental: Boolean? = null
    private var image: File? = null
    private var imageUrl: String? = null
    private var fromDate: String? = null
    private var toDate: String? = null
    private var firstTime = true
    private val navArgs by navArgs<EditServiceStoreFragmentArgs>()
    private lateinit var adapterCategories: ArrayAdapter<String>
    private lateinit var adapterSubCategories: ArrayAdapter<String>
    private lateinit var adapterRental: ArrayAdapter<String>

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var factory: EditServiceViewModel.EditServiceAssistedFactory
    private val viewModel by viewModels<EditServiceViewModel> {
        EditServiceViewModel.createEditServiceFactory(factory, navArgs.serviceId)
    }


    private val requestPermissionGalleryLauncher =
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditServiceStoreBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)


        fromDate = getTodayDate()
        toDate = getTodayDate()
        binding.txtFromDateValue.text = getTodayDateLanguage(sharedPreferences)
        binding.txtToDateValue.text = getTodayDateLanguage(sharedPreferences)
        viewModel.visibilityServiceRental(isRental ?: false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.editItemServiceUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.focusError) {
                    if (viewModel.editItemServiceUiState.value.titleError)
                        binding.txtTitleSellService.scroll()
                    else if (viewModel.editItemServiceUiState.value.categoryError)
                        binding.txtCategorySellService.scroll()
                    else if (viewModel.editItemServiceUiState.value.subCategoryError)
                        binding.txtSubCategorySellService.scroll()
                    else if (viewModel.editItemServiceUiState.value.descriptionError)
                        binding.txtItemDescriptionSellService.scroll()
                    else if (viewModel.editItemServiceUiState.value.priceError)
                        binding.txtPriceSellService.scroll()
                    else if (viewModel.editItemServiceUiState.value.locationError)
                        binding.txtLocationSellService.scroll()
                    else if (viewModel.editItemServiceUiState.value.pictureError)
                        binding.txtPictureSellService.scroll()
                    else
                        binding.checkboxAgreeSellStoreService.scroll()
                }
                if (it.addProductMsg != null) {
                    Toast.makeText(context, it.addProductMsg, Toast.LENGTH_SHORT).show()
                    viewModel.resetMsg()
                    if (it.isSucceedEditProduct)
                        findNavController().popBackStack(R.id.serviceDetailsOwnerFragment, true)
                }
                if (it.sellCategoriesList.isNotEmpty() && adapterCategories.isEmpty) {
                    if (it.product != null) {
                        categoryId = it.product.categoryId
                        subCategoryId = it.product.subCategoryId
                        binding.edtCategorySellService.setText(it.product.category)
                        adapterCategories.addAll(it.sellCategoriesList.map { data -> data.name })
                        binding.edtCategorySellService.setAdapter(adapterCategories)
                        viewModel.getSubCategories(categoryId)
                    }
                }
                if (it.sellSubCategoriesList.isNotEmpty() && adapterSubCategories.isEmpty) {
                    val d = it.sellSubCategoriesList.map { data -> data.name }
                    if (it.product != null) {
                        adapterSubCategories.addAll(d)
                        binding.edtSubCategorySellService.setAdapter(adapterSubCategories)
                    }
                }
                if (it.product != null) {
                    if (firstTime) {
                        binding.edtTitleSell.setText(it.product.title)
                        binding.edtItemDescriptionSellService.setText(it.product.serviceDescription)
                        binding.edtPriceSellService.setText(it.product.price.toString())
                        binding.edtLocationSellService.setText(it.product.location)
                        binding.edtPictureSellService.setText(it.product.serviceImageUrl)
                        binding.edtSubCategorySellService.setText(it.product.subCategory)
                        imageUrl = it.product.serviceImageUrl
                        val listIsRental =
                            resources.getStringArray(R.array.arrayServiceRental).toList()
                        if (it.product.itIsARent) {
                            fromDate = it.product.rentFrom
                            toDate = it.product.rentTo
                            isRental = true
                            binding.edtIsServiceRental.setText(listIsRental[0])
                            adapterRental.addAll(listIsRental)
                            binding.edtIsServiceRental.setAdapter(adapterRental)
                            viewModel.visibilityServiceRental(true)
                            binding.txtFromDateValue.text = it.product.rentFrom
                            binding.txtToDateValue.text = it.product.rentTo
                        } else {
                            isRental = false
                            binding.edtIsServiceRental.setText(listIsRental[1])
                            adapterRental.addAll(listIsRental)
                            binding.edtIsServiceRental.setAdapter(adapterRental)
                            viewModel.visibilityServiceRental(false)
                        }
                        firstTime = false
                    }
                }
            }
        }
        binding.edtCategorySellService.setOnItemClickListener { _, _, position, _ ->
            if (categoryId != viewModel.editItemServiceUiState.value.sellCategoriesList[position].id) {
                binding.edtSubCategorySellService.text.clear()
                categoryId = viewModel.editItemServiceUiState.value.sellCategoriesList[position].id
                adapterSubCategories =
                    ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
                viewModel.getSubCategories(categoryId)
            }
        }
        binding.edtSubCategorySellService.setOnItemClickListener { _, _, position, _ ->
            if (subCategoryId != viewModel.editItemServiceUiState.value.sellSubCategoriesList[position].id) {
                subCategoryId =
                    viewModel.editItemServiceUiState.value.sellSubCategoriesList[position].id
            }
        }
        binding.edtIsServiceRental.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    viewModel.visibilityServiceRental(true)
                    sharedViewModel.setVisibilityIsRental(true)
                    isRental = true
                }

                1 -> {
                    viewModel.visibilityServiceRental(false)
                    sharedViewModel.setVisibilityIsRental(false)
                    isRental = false
                }
            }
        }
        binding.checkboxAgreeSellStoreService.setOnCheckedChangeListener { _, isChecked ->
            if (viewModel.editItemServiceUiState.value.conditionAndTermsError) {
                if (isChecked)
                    viewModel.updateStateTermsAndCondition()
            }
        }
        binding.btnChoosePictureService.setOnClickListener {
            showDialog()
        }
        binding.btnSubmitSellStoreService.setOnClickListener {
            addItem()
        }
        binding.txtFromDateValue.setOnClickListener {
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
        binding.txtToDateValue.setOnClickListener {
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
        loadData()
    }

    private fun addItem() {
        viewModel.editItem(
            id = viewModel.editItemServiceUiState.value.product!!.id,
            title = binding.edtTitleSell.text.toString(),
            categoryId = categoryId,
            subCategoryId = subCategoryId,
            itemDescription = binding.edtItemDescriptionSellService.text.toString(),
            price = binding.edtPriceSellService.text.toString(),
            location = binding.edtLocationSellService.text.toString(),
            productImage = image,
            isServiceRental = isRental,
            fromDate = fromDate ?: "",
            toDate = toDate ?: "",
            accept = binding.checkboxAgreeSellStoreService.isChecked,
            imageUrl = imageUrl
        )
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 3)
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

    private fun View.scroll() {
        binding.scrollSellStoreService.smoothScrollTo(0, this.top)
    }

    private fun loadData() {
        binding.edtCategorySellService.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtSubCategorySellService.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtIsServiceRental.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        adapterCategories =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        adapterSubCategories =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        adapterRental =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        viewModel.getAllData()
    }

    private fun pickImageFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 1)
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

    private fun checkExternalPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            )
                pickImageFromGallery()
            else
                requestPermissionGalleryLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            )
                pickImageFromGallery()
            else
                requestPermissionGalleryLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
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
}