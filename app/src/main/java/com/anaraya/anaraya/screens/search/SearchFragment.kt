package com.anaraya.anaraya.screens.search

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentSearchBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.search.adapter.SearchAdapter
import com.anaraya.anaraya.screens.shared_interaction.ProductInteractionListener
import com.anaraya.anaraya.util.plusNumBasket
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showButtonCart
import com.anaraya.anaraya.util.showButtonFav
import com.anaraya.anaraya.util.showButtonFilter
import com.anaraya.anaraya.util.showCardHome
import com.anaraya.anaraya.util.showTextBadge
import com.anaraya.anaraya.util.showTextSchedule
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment : Fragment(), ProductInteractionListener {
    private val navArgs: SearchFragmentArgs by navArgs()
    private lateinit var binding: FragmentSearchBinding
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnFilter: ImageButton
    private lateinit var btnReload: Button
    private lateinit var edtSearch: EditText
    private var lang: String? = null
    private var word: String = ""
    private lateinit var adapter: SearchAdapter
    private var pos = -1
    @Inject
    lateinit var factory: SearchViewModel.AssistedFactory
    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.createProvider(
            navArgs.id,
            navArgs.selectionType,
            factory
        )
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnFilter = requireActivity().findViewById(R.id.btnFilter)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        edtSearch = requireActivity().findViewById(R.id.edtSearch)
        adapter = SearchAdapter(this)
        binding.recyclerSearch.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.navigateToFilter) {
                    findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToFilterFragment())
                    viewModel.navigateToFilterDone()
                }
                if (it.addCartUiState != null) {
                    if (pos > -1) {
                        //Toast.makeText(context, it.addCartUiState, Toast.LENGTH_SHORT).show()
                        adapter.changeIcon(pos)
                        pos = -1
                        plusNumBasket(sharedPreferences,sharedViewModel,requireContext())
                    }
                }
                if (it.products != null) {
                    it.products.collectLatest { data ->
                        adapter.submitData(data)
                    }
                }
            }
        }

        lifecycleScope.launch {
            sharedViewModel.homeState.collectLatest {
                if (it.getSearchData){
                    val listCat = sharedViewModel.homeState.value.listCat
                    val listBrand = sharedViewModel.homeState.value.listBrand
                    viewModel.search(word, lang,
                        listCat.ifEmpty { null }, listBrand.ifEmpty { null }, getDesc())
                    sharedViewModel.getSearchData(false)
                }
            }
        }
        edtSearch.addTextChangedListener(textWatcher())

        adapter.addLoadStateListener { loadState ->
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                viewModel.showLoading(false)
                if (it.error.message == getString(R.string.not_found)) {
                    if (adapter.snapshot().isEmpty())
                        viewModel.setError(it.error.message)
                } else {
                    viewModel.setError(it.error.message)
                }
            }
        }
        btnFilter.setOnClickListener {
            viewModel.navigateToFilter()
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
        showCardHome(requireActivity(),true)
        showBottomNavBar(requireActivity(), false)
        showTextSchedule(requireActivity(), false)
        showTextBadge(requireActivity(), false, sharedPreferences, requireContext())
        showButtonCart(requireActivity(), false)
        showButtonFilter(requireActivity(), true)
        showButtonFav(requireActivity(), false)
        showToolBar(requireActivity(), false)
        // to hide text badge
        sharedViewModel.setProductInBasket(0)
    }


    private fun reload() {
        val listCat = sharedViewModel.homeState.value.listCat
        val listBrand = sharedViewModel.homeState.value.listBrand
        sharedViewModel.reloadClick()
        viewModel.getAllData(word, lang,
            listCat.ifEmpty { null }, listBrand.ifEmpty { null }, getDesc()
        )
        sharedViewModel.reloadClickDone()
    }

    override fun onCLick(productId: Int) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToProductDetailsFragment(
                productId
            )
        )
    }

    override fun addToCart(productId: Int, position: Int) {
        pos = position
        viewModel.addToCart(productId, 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        edtSearch.setText("")
        edtSearch.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edtSearch.windowToken, 0)
        edtSearch.clearFocus()
    }

    private fun textWatcher(): TextWatcher =
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputText = s.toString()
                if (inputText.matches(Regex("[A-Za-z]+"))) {
                    // English
                    // Handle English input
                    lang = "e"
                } else if (inputText.matches(Regex("\\p{InArabic}+"))) {
                    // Arabic
                    // Handle Arabic input
                    lang = "a"
                } else if (inputText.matches(Regex("[\\p{InBasicLatin}\\p{InLatin-1Supplement}]+"))) {
                    // French
                    // Handle French input
                    lang = "f"
                } else {
                    // Unknown language or mixed
                    // Handle other cases
                    lang = null
                }
                word = s.toString()
                val listCat = sharedViewModel.homeState.value.listCat
                val listBrand = sharedViewModel.homeState.value.listBrand
                viewModel.search(s.toString(), lang,
                    listCat.ifEmpty { null }, listBrand.ifEmpty { null }, getDesc()
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

    private fun getDesc(): Int? {
        return if (sharedViewModel.homeState.value.priceHighest && sharedViewModel.homeState.value.priceLowest)
            null
        else if (sharedViewModel.homeState.value.priceHighest)
            1
        else if (sharedViewModel.homeState.value.priceLowest)
            0
        else
            null
    }
}