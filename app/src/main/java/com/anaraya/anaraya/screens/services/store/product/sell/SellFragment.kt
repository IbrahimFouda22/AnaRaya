package com.anaraya.anaraya.screens.services.store.product.sell

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import com.anaraya.anaraya.databinding.FragmentSellBinding
import com.anaraya.anaraya.databinding.LayoutDialogImageBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.RealPathUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SellFragment : Fragment() {
    private lateinit var binding: FragmentSellBinding
    private val viewModel by viewModels<SellViewModel>()
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private var categoryId: Int = -1
    private var subCategoryId: Int = -1
    private var condition: Int = -1
    private var isAnonymous: Boolean? = null
    private var handleDelivery: Boolean? = null
    private var image: File? = null
    private lateinit var adapterCategories : ArrayAdapter<String>
    private lateinit var adapterSubCategories : ArrayAdapter<String>

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSellBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.sellUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.focusError) {
                    binding.root.clearFocus()
                    if (viewModel.sellUiState.value.titleError)
                        binding.txtTitleSell.scroll()
                    else if (viewModel.sellUiState.value.categoryError)
                        binding.txtCategorySell.scroll()
                    else if (viewModel.sellUiState.value.subCategoryError)
                        binding.txtSubCategorySell.scroll()
                    else if (viewModel.sellUiState.value.conditionError)
                        binding.txtConditionSell.scroll()
                    else if (viewModel.sellUiState.value.descriptionError)
                        binding.txtItemDescriptionSell.scroll()
                    else if (viewModel.sellUiState.value.priceError)
                        binding.txtPriceSell.scroll()
                    else if (viewModel.sellUiState.value.locationError)
                        binding.txtLocationSell.scroll()
                    else if (viewModel.sellUiState.value.anonymousError)
                        binding.txtAnonymous.scroll()
                    else if (viewModel.sellUiState.value.deliveryError)
                        binding.txtHandleDelivery.scroll()
                    else if (viewModel.sellUiState.value.pictureError)
                        binding.txtPictureSell.scroll()
                    else
                        binding.checkboxAgreeSellStore.scroll()
                }
                if (it.addProductMsg != null) {
                    Toast.makeText(context, it.addProductMsg, Toast.LENGTH_SHORT).show()
                    viewModel.resetMsg()
                }
                if (it.sellCategoriesList.isNotEmpty() && adapterCategories.isEmpty) {
                    adapterCategories.addAll(it.sellCategoriesList.map { data -> data.name })
                    binding.edtCategorySell.setAdapter(adapterCategories)
                }
                if (it.sellSubCategoriesList.isNotEmpty() && adapterSubCategories.isEmpty) {
                    val d = it.sellSubCategoriesList.map { data -> data.name }
                    adapterSubCategories.addAll(d)
                    binding.edtSubCategorySell.setAdapter(adapterSubCategories)
                }
            }
        }
        binding.edtCategorySell.setOnItemClickListener { _, _, position, _ ->
            if (categoryId != viewModel.sellUiState.value.sellCategoriesList[position].id) {
                binding.edtSubCategorySell.text.clear()
                categoryId = viewModel.sellUiState.value.sellCategoriesList[position].id
                adapterSubCategories =
                    ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
                viewModel.getSubCategories(categoryId)
            }
        }
        binding.edtSubCategorySell.setOnItemClickListener { _, _, position, _ ->
            if (subCategoryId != viewModel.sellUiState.value.sellSubCategoriesList[position].id) {
                subCategoryId = viewModel.sellUiState.value.sellSubCategoriesList[position].id
            }
        }
        binding.edtConditionSell.setOnItemClickListener { _, _, position, _ ->
            if (condition != position) {
                condition = position
            }
        }
        binding.edtAnonymous.setOnItemClickListener { _, _, position, _ ->
            when(position){
                0 -> isAnonymous = true
                1 -> isAnonymous = false
            }
        }
        binding.edtHandleDelivery.setOnItemClickListener { _, _, position, _ ->
            when(position){
                0 -> handleDelivery = true
                1 -> handleDelivery = false
            }
        }
        binding.checkboxAgreeSellStore.setOnCheckedChangeListener { _, isChecked ->
            if(sharedViewModel.homeState.value.isEnteredToTermsAndCondition){
                if (viewModel.sellUiState.value.conditionAndTermsError) {
                    if (isChecked)
                        viewModel.updateStateTermsAndCondition()
                }
            }
            else{
                if (isChecked)
                    viewModel.updateStateTermsAndCondition()
                viewModel.resetErrors()
                sharedViewModel.setTermsAndCondition(true)
                sharedViewModel.navigateToTermsAndCondition()
            }
        }
        binding.btnChoosePicture.setOnClickListener {
            showDialog()
        }
        binding.btnSubmitSellStore.setOnClickListener {
            if(!viewModel.sellUiState.value.isLoading)
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
        binding.edtCategorySell.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtSubCategorySell.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtConditionSell.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtAnonymous.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtHandleDelivery.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        adapterCategories =  ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        adapterSubCategories =ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        val adapterAnonymous =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        val adapterHandleDelivery =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        binding.edtAnonymous.setAdapter(adapterAnonymous)
        binding.edtHandleDelivery.setAdapter(adapterHandleDelivery)

        adapterAnonymous.addAll(resources.getStringArray(R.array.arrayAnonymous).toList())
        adapterHandleDelivery.addAll(resources.getStringArray(R.array.arrayHandleDelivery).toList())
        val adapterConditions =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        adapterConditions.add(getString(R.string.neww))
        adapterConditions.add(getString(R.string.used))
        binding.edtConditionSell.setAdapter(adapterConditions)
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
            Log.d("ImageName",file.name)
            binding.edtPictureSell.setText(file.name)
            image = file
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val img = data?.extras?.get("data")

            val file = crateFileFromBitmap(img as Bitmap,requireContext())
            binding.edtPictureSell.setText(file.name)
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

    private fun addItem() {
        viewModel.addItem(
            binding.edtTitleSell.text.toString(),
            categoryId,
            subCategoryId,
            if(condition == -1) -1 else condition + 1,
            binding.edtItemDescriptionSell.text.toString(),
            binding.edtPriceSell.text.toString(),
            binding.edtLocationSell.text.toString(),
            isAnonymous,
            handleDelivery,
            image,
            binding.checkboxAgreeSellStore.isChecked,
        )
    }

    private fun View.scroll() {
        binding.scrollSellStore.smoothScrollTo(0, this.top)
    }

}

fun crateFileFromBitmap(bitmap: Bitmap,context: Context): File {
    val file = File(context.cacheDir, "img170080070.png")
    file.outputStream().use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }
    return file
}