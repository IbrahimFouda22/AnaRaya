package com.anaraya.anaraya.home.more.help

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentHelpBinding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.anaraya.anaraya.home.more.help.adapter.HelpAdapter
import com.anaraya.anaraya.home.more.help.interaction.HelpInteraction
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HelpFragment : Fragment(),HelpInteraction {

    private lateinit var binding: FragmentHelpBinding
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val viewModel by viewModels<HelpViewModel>({ this })
    private var list = emptyList<HelpUiStateData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHelpBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        val adapter = HelpAdapter(this)
        binding.recyclerHelp.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.helpUiState.collectLatest {

                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.helpUiStateList.isEmpty()){
                    adapter.submitList(emptyList())
                }
                if(it.helpUiStateList.isNotEmpty()){
                    if (list.isEmpty())
                        list = it.helpUiStateList
                    adapter.submitList(it.helpUiStateList)
                }
            }
        }

        binding.edtSearchHelp.addTextChangedListener(textWatcher())
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
        showToolBar(requireActivity(),true)
        showBottomNavBar(requireActivity(),false)
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        showToolBar(requireActivity(),true)
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    private fun textWatcher():TextWatcher = object :TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            if (s.isNullOrEmpty())
                viewModel.update(list)
            else{
                val filteredList = list.filter {
                    it.topic!!.contains(s.toString(),true)
                }
                viewModel.update(filteredList)
                Log.d("list", "$filteredList")
            }
        }

    }

    override fun onClick(topic: HelpUiStateData) {
        findNavController().navigate(HelpFragmentDirections.actionHelpFragment2ToHelpDetailsFragment(topic))
    }
}