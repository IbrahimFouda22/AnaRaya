package com.anaraya.anaraya.screens.services.store.service.explore.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentSearchServiceStoreBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.services.store.adapter.ExploreServiceAdapter
import com.anaraya.anaraya.screens.services.store.interaction.ExploreServiceInteraction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchServiceStoreFragment : Fragment(), ExploreServiceInteraction {
    private lateinit var binding: FragmentSearchServiceStoreBinding
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private var lang: String? = null
    private var word: String = ""
    private lateinit var adapter: ExploreServiceAdapter
    private val viewModel: SearchServiceViewModel by viewModels()
    private val navArgs by navArgs<SearchServiceStoreFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchServiceStoreBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        adapter = ExploreServiceAdapter(this)
        binding.recyclerExploreProduct.adapter = adapter

        binding.edtSearch.addTextChangedListener(textWatcher())
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.products != null) {
                    it.products.collectLatest { data ->
                        adapter.submitData(data)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collect {
                viewModel.showLoading(it.refresh is LoadState.Loading)
            }
        }

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
        binding.btnBackAllExploreProduct.setOnClickListener {
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
                viewModel.getAllProduct(
                    searchWord = word,
                    searchLanguage = lang ?: "e",
                    catId = navArgs.catId
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

    override fun onClick(serviceId: Int) {
        findNavController().navigate(
            SearchServiceStoreFragmentDirections.actionSearchServiceStoreFragmentToExploreServiceDetailsFragment(
                serviceId
            )
        )
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllProduct(searchWord = word, searchLanguage = lang ?: "e",navArgs.catId)
        sharedViewModel.reloadClickDone()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}