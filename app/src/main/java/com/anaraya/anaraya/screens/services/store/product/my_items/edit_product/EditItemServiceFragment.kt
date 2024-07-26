package com.anaraya.anaraya.screens.services.store.product.my_items.edit_product

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentEditItemServiceBinding
import com.anaraya.anaraya.databinding.LayoutDialogImageBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.services.store.product.sell.crateFileFromBitmap
import com.anaraya.anaraya.util.RealPathUtil
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class EditItemServiceFragment : Fragment() {
    private lateinit var binding: FragmentEditItemServiceBinding
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private var categoryId: Int = -1
    private var subCategoryId: Int = -1
    private var condition: Int = -1
    private var isAnonymous: Boolean? = null
    private var handleDelivery: Boolean? = null
    private var image: File? = null
    private var imageUrl: String? = null
    private var firstTime = true
    private val navArgs by navArgs<EditItemServiceFragmentArgs>()
    private lateinit var adapterCategories: ArrayAdapter<String>
    private lateinit var adapterSubCategories: ArrayAdapter<String>
    private lateinit var adapterConditions: ArrayAdapter<String>
    private lateinit var adapterAnonymous: ArrayAdapter<String>
    private lateinit var adapterHandleDelivery: ArrayAdapter<String>

    @Inject
    lateinit var factory: EditItemServiceViewModel.EditProductAssistedFactory
    private val viewModel by viewModels<EditItemServiceViewModel> {
        EditItemServiceViewModel.createEditProductFactory(factory, navArgs.productId)
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
        binding = FragmentEditItemServiceBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.editItemServiceUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.focusError) {
                    if (viewModel.editItemServiceUiState.value.titleError)
                        binding.txtTitleSell.scroll()
                    else if (viewModel.editItemServiceUiState.value.categoryError)
                        binding.txtCategorySell.scroll()
                    else if (viewModel.editItemServiceUiState.value.subCategoryError)
                        binding.txtSubCategorySell.scroll()
                    else if (viewModel.editItemServiceUiState.value.conditionError)
                        binding.txtConditionSell.scroll()
                    else if (viewModel.editItemServiceUiState.value.descriptionError)
                        binding.txtItemDescriptionSell.scroll()
                    else if (viewModel.editItemServiceUiState.value.priceError)
                        binding.txtPriceSell.scroll()
                    else if (viewModel.editItemServiceUiState.value.locationError)
                        binding.txtLocationSell.scroll()
                    else if (viewModel.editItemServiceUiState.value.anonymousError)
                        binding.txtAnonymous.scroll()
                    else if (viewModel.editItemServiceUiState.value.deliveryError)
                        binding.txtHandleDelivery.scroll()
                    else if (viewModel.editItemServiceUiState.value.pictureError)
                        binding.txtPictureSell.scroll()
                    else
                        binding.checkboxAgreeSellStore.scroll()
                }
                if (it.addProductMsg != null) {
                    Toast.makeText(context, it.addProductMsg, Toast.LENGTH_SHORT).show()
                    viewModel.resetMsg()
                    if (it.isSucceedEditProduct)
                        findNavController().popBackStack(R.id.itemDetailsFragment, true)
                }
                if (it.sellCategoriesList.isNotEmpty() && adapterCategories.isEmpty) {
                    if (it.product != null) {
                        categoryId = it.product.categoryId
                        subCategoryId = it.product.subCategoryId
                        binding.edtCategorySell.setText(it.product.category)
                        adapterCategories.addAll(it.sellCategoriesList.map { data -> data.name })
                        binding.edtCategorySell.setAdapter(adapterCategories)
                        viewModel.getSubCategories(categoryId)
                    }
                }
                if (it.sellSubCategoriesList.isNotEmpty() && adapterSubCategories.isEmpty) {
                    val d = it.sellSubCategoriesList.map { data -> data.name }
                    if (it.product != null) {
                        adapterSubCategories.addAll(d)
                        binding.edtSubCategorySell.setAdapter(adapterSubCategories)
                    }
                }
                if (it.product != null) {
                    if (firstTime) {
                        binding.edtTitleSell.setText(it.product.title)
                        binding.edtItemDescriptionSell.setText(it.product.itemDescription)
                        binding.edtPriceSell.setText(it.product.price.toString())
                        binding.edtLocationSell.setText(it.product.location)
                        binding.edtPictureSell.setText(it.product.productImageUrl)
                        binding.edtSubCategorySell.setText(it.product.subCategory)
                        imageUrl = it.product.productImageUrl
                        val listHandleDelivery =
                            resources.getStringArray(R.array.arrayHandleDelivery).toList()
                        val listAnonymous =
                            resources.getStringArray(R.array.arrayAnonymous).toList()
                        val listCondition: MutableList<String> = mutableListOf()
                        listCondition.add(getString(R.string.neww))
                        listCondition.add(getString(R.string.used))

                        if (it.product.condition == 1) {
                            condition = 0
                            binding.edtConditionSell.setText(listCondition[0])
                            adapterConditions.addAll(listCondition)
                            binding.edtConditionSell.setAdapter(adapterConditions)
                        } else {
                            condition = 1
                            binding.edtConditionSell.setText(listCondition[1])
                            adapterConditions.addAll(listCondition)
                            binding.edtConditionSell.setAdapter(adapterConditions)
                        }

                        if (it.product.isAnonymous) {
                            isAnonymous = true
//                        binding.edtAnonymous.setText(resources.getStringArray(R.array.arrayAnonymous).toList()[0])
                            binding.edtAnonymous.setText(listAnonymous[0])
                            adapterAnonymous.addAll(listAnonymous)
                            binding.edtAnonymous.setAdapter(adapterAnonymous)
                        } else {
                            isAnonymous = false
//                        binding.edtAnonymous.setText(resources.getStringArray(R.array.arrayAnonymous).toList()[1])
                            binding.edtAnonymous.setText(listAnonymous[1])
                            adapterAnonymous.addAll(listAnonymous)
                            binding.edtAnonymous.setAdapter(adapterAnonymous)
                        }

                        if (it.product.handleDelivery) {
                            handleDelivery = true
                            binding.edtHandleDelivery.setText(listHandleDelivery[0])
                            adapterHandleDelivery.addAll(listHandleDelivery)
                            binding.edtHandleDelivery.setAdapter(adapterHandleDelivery)
                        } else {
                            handleDelivery = false
                            binding.edtHandleDelivery.setText(listHandleDelivery[1])
                            adapterHandleDelivery.addAll(listHandleDelivery)
                            binding.edtHandleDelivery.setAdapter(adapterHandleDelivery)
                        }
                        firstTime = false
                    }
                }
            }
        }
        binding.edtCategorySell.setOnItemClickListener { _, _, position, _ ->
            if (categoryId != viewModel.editItemServiceUiState.value.sellCategoriesList[position].id) {
                binding.edtSubCategorySell.text.clear()
                categoryId = viewModel.editItemServiceUiState.value.sellCategoriesList[position].id
                adapterSubCategories =
                    ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
                viewModel.getSubCategories(categoryId)
            }
        }
        binding.edtSubCategorySell.setOnItemClickListener { _, _, position, _ ->
            if (subCategoryId != viewModel.editItemServiceUiState.value.sellSubCategoriesList[position].id) {
                subCategoryId =
                    viewModel.editItemServiceUiState.value.sellSubCategoriesList[position].id
            }
        }
        binding.edtConditionSell.setOnItemClickListener { _, _, position, _ ->
            if (condition != position) {
                condition = position
            }
        }
        binding.edtAnonymous.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> isAnonymous = true
                1 -> isAnonymous = false
            }
        }
        binding.edtHandleDelivery.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> handleDelivery = true
                1 -> handleDelivery = false
            }
        }
        binding.checkboxAgreeSellStore.setOnCheckedChangeListener { _, isChecked ->
            if (viewModel.editItemServiceUiState.value.conditionAndTermsError) {
                if (isChecked)
                    viewModel.updateStateTermsAndCondition()
            }
        }
        binding.btnChoosePicture.setOnClickListener {
            showDialog()
        }
        binding.btnSubmitSellStore.setOnClickListener {
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
        showCardHome(requireActivity(), false)
        showBottomNavBar(requireActivity(), false)
        loadData()
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

    private fun addItem() {
        viewModel.editItem(
            id = navArgs.productId,
            title = binding.edtTitleSell.text.toString(),
            categoryId = categoryId,
            subCategoryId = subCategoryId,
            condition = condition + 1,
            itemDescription = binding.edtItemDescriptionSell.text.toString(),
            price = binding.edtPriceSell.text.toString(),
            location = binding.edtLocationSell.text.toString(),
            isAnonymous = isAnonymous,
            handleDelivery = handleDelivery,
            productImage = image,
            accept = binding.checkboxAgreeSellStore.isChecked,
            imageUrl = imageUrl
        )
    }

    private fun View.scroll() {
        binding.scrollSellStore.smoothScrollTo(0, this.top)
    }

    private fun loadData() {
        binding.edtCategorySell.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtSubCategorySell.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtConditionSell.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtAnonymous.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtHandleDelivery.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        adapterCategories =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        adapterSubCategories =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        viewModel.getAllData()
        adapterConditions =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        adapterAnonymous =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        adapterHandleDelivery = ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
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