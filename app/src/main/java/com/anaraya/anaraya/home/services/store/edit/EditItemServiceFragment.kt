package com.anaraya.anaraya.home.services.store.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentEditItemServiceBinding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.pupilJ.jk.util.RealPathUtil
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

@Suppress("DEPRECATION")
class EditItemServiceFragment : Fragment() {
    private val navArgs by navArgs<EditItemServiceFragmentArgs>()
    private lateinit var binding: FragmentEditItemServiceBinding
    private val viewModel by viewModels<EditItemServiceViewModel>()
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private var categoryId: Int = -1
    private var subCategoryId: Int = -1
    private var condition: Int = -1
    private var image: File? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditItemServiceBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        val adapterCategories =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        var adapterSubCategories =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.editItemServiceUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
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
                    else if (viewModel.editItemServiceUiState.value.pictureError)
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
                    binding.edtCategorySell.setText(
                        sharedViewModel.homeState.value.itemSell!!.category, false
                    )
                }
                if (it.sellSubCategoriesList.isNotEmpty() && adapterSubCategories.isEmpty) {
                    val d = it.sellSubCategoriesList.map { data -> data.name }
                    adapterSubCategories.addAll(d)
                    binding.edtSubCategorySell.setAdapter(adapterSubCategories)
                    binding.edtSubCategorySell.setText(
                        sharedViewModel.homeState.value.itemSell!!.subCategory, false
                    )
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
        binding.checkboxAgreeSellStore.setOnCheckedChangeListener { _, isChecked ->
            if (viewModel.editItemServiceUiState.value.conditionAndTermsError) {
                if (isChecked)
                    viewModel.updateStateTermsAndCondition()
            }
        }
        binding.btnChoosePicture.setOnClickListener {
            pickImageFromGallery()
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
        viewModel.getAllData(navArgs.isProducts)
        val adapterConditions =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        adapterConditions.add(getString(R.string.neww))
        adapterConditions.add(getString(R.string.used))
        binding.edtConditionSell.setAdapter(adapterConditions)
        binding.edtConditionSell.setText(
            if (sharedViewModel.homeState.value.itemSell!!.condition == 1) getString(
                R.string.neww
            ) else getText(R.string.used), false
        )
        viewModel.setItem(sharedViewModel.homeState.value.itemSell)
        binding.edtCategorySell.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtSubCategorySell.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        binding.edtConditionSell.setDropDownBackgroundResource(R.drawable.shape_drop_down)
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllData(navArgs.isProducts)
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
    }

    private fun addItem() {
        viewModel.addItem(
            binding.edtTitleSell.text.toString(),
            categoryId,
            subCategoryId,
            condition + 1,
            binding.edtItemDescriptionSell.text.toString(),
            binding.edtPriceSell.text.toString(),
            binding.edtLocationSell.text.toString(),
            image,
            binding.checkboxAgreeSellStore.isChecked,
            navArgs.isProducts
        )
    }

    private fun View.scroll() {
        binding.scrollSellStore.smoothScrollTo(0, this.top)
    }
}