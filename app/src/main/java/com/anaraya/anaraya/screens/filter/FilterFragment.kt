package com.anaraya.anaraya.screens.filter

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentFilterBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.category_product.CategoryUiState
import com.anaraya.anaraya.util.showCardHome
import com.anaraya.anaraya.util.showToolBar
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.checkbox.MaterialCheckBox
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FilterFragment : Fragment() {

    private lateinit var binding: FragmentFilterBinding
    private val viewModel by viewModels<FilterViewModel>()
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private var highest = false
    private var lowest = false


    //private val listPrice = arrayListOf<Int>()
    private val listCat = arrayListOf<Int>()
    private val listBrand = arrayListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFilterBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        highest = sharedViewModel.homeState.value.priceHighest
        lowest = sharedViewModel.homeState.value.priceLowest
        sharedViewModel.homeState.value.listCat.toCollection(listCat)
        sharedViewModel.homeState.value.listBrand.toCollection(listBrand)
        binding.checkboxHighest.isChecked = highest
        binding.checkboxLowest.isChecked = lowest

        binding.checkboxHighest.addOnCheckedStateChangedListener(checkedListener(getString(R.string.prices)))
        binding.checkboxLowest.addOnCheckedStateChangedListener(checkedListener(getString(R.string.prices)))
        lifecycleScope.launch {
            viewModel.filterUiState.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.listCatsState.isNotEmpty()) {
                    for (item in it.listCatsState) {
                        binding.consCatFilter.addView(
                            createCheckBoxView(
                                item,
                                getString(R.string.categories),
                                sharedViewModel.homeState.value.listCat.contains(item.id)
                            )
                        )
                    }
                }
                if (it.listBrandsState.isNotEmpty()) {
                    for (item in it.listBrandsState) {
                        binding.consBrandFilter.addView(
                            createCheckBoxView(
                                item,
                                getString(R.string.brand),
                                sharedViewModel.homeState.value.listBrand.contains(item.id)
                            )
                        )
                    }
                }
            }
        }
        binding.btnFilter.setOnClickListener {
            sharedViewModel.setHighest(binding.checkboxHighest.isChecked)
            sharedViewModel.setLowest(binding.checkboxLowest.isChecked)
            sharedViewModel.setLisBrand(listBrand)
            sharedViewModel.setLisCat(listCat)
            sharedViewModel.getSearchData(true)
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

    @SuppressLint("InflateParams")
    private fun createCheckBoxView(item: CategoryUiState, type: String, isChecked: Boolean): View {
        val layout =
            layoutInflater.inflate(R.layout.layout_item_check_box, null) as MaterialCheckBox
        layout.text = item.name
        layout.id = item.id
        layout.isChecked = isChecked
        layout.addOnCheckedStateChangedListener(checkedListener(type))
        return layout
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        showToolBar(requireActivity(), true)
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    override fun onStart() {
        super.onStart()
        val toolBar = requireActivity().findViewById<MaterialToolbar>(R.id.toolBarActivity)
        showCardHome(requireActivity(), false)
        showToolBar(requireActivity(), true)
        toolBar.setNavigationIcon(R.drawable.ic_close_bottom_sheet)
    }


    private fun checkedListener(type: String) = when (type) {
        getString(R.string.prices) -> MaterialCheckBox.OnCheckedStateChangedListener { checkBox, _ ->
//            if(listPrice.contains(checkBox.id))
//                listPrice.remove(checkBox.id)
//            else
//                listPrice.add(checkBox.id)

            if (checkBox.id == R.id.checkboxHighest)
                highest = checkBox.isChecked
            else
                lowest = checkBox.isChecked

        }

        getString(R.string.categories) -> MaterialCheckBox.OnCheckedStateChangedListener { checkBox, _ ->
            if (listCat.contains(checkBox.id)) {
                if (listCat.size == 1)
                    listCat.clear()
                else
                    listCat.remove(checkBox.id)
            } else
                listCat.add(checkBox.id)
        }

        else -> MaterialCheckBox.OnCheckedStateChangedListener { checkBox, _ ->
            if (listBrand.contains(checkBox.id)) {
                if (listBrand.size == 1)
                    listBrand.clear()
                else
                    listBrand.remove(checkBox.id)
            } else
                listBrand.add(checkBox.id)
        }
    }

}