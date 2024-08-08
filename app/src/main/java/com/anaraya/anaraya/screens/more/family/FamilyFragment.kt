package com.anaraya.anaraya.screens.more.family

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentFamilyBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showToolBar
import com.anaraya.anaraya.util.visible
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FamilyFragment : Fragment() {
    private val viewModel by viewModels<FamilyViewModel>({ this })
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private lateinit var binding: FragmentFamilyBinding
    private lateinit var toolBar: MaterialToolbar
    private var selectedPosition = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFamilyBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        toolBar = requireActivity().findViewById(R.id.toolBarActivity)

        toolBar.inflateMenu(R.menu.menu_family)
        toolBar.visible()
        toolBar.setOnMenuItemClickListener(onMenuItemClickListener())

        binding.edtRelationship.setDropDownBackgroundResource(R.drawable.shape_drop_down)
        val adapterAllRelationships =
            ArrayAdapter<String>(requireContext(), R.layout.layout_item_company_address)
        binding.edtRelationship.setAdapter(adapterAllRelationships)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.familyUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.addReferralMsg != null) {
                    Toast.makeText(context, it.addReferralMsg, Toast.LENGTH_SHORT).show()
                }
                if (it.listRelationships.isNotEmpty()) {
                    adapterAllRelationships.addAll(it.listRelationships.map { relationship ->
                        relationship.name
                    })
                }
            }
        }
        binding.edtRelationship.setOnItemClickListener { _, _, position, _ ->
            selectedPosition = position
        }
        binding.btnSendFamily.setOnClickListener {
            sendFamily()
        }


        btnBack.setOnClickListener {
            reload()
        }
        btnReload.setOnClickListener {
            reload()
        }

        return binding.root
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        showToolBar(requireActivity(), true)
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    override fun onStart() {
        super.onStart()
        showBottomNavBar(requireActivity(),false)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        toolBar.menu.clear()
        binding.unbind()
    }

    private fun sendFamily() {
        viewModel.addNewReferral(
            binding.edtNameFamily.text.toString(),
            binding.edtPhoneFamily.text.toString(),
            if (selectedPosition == -1) null else viewModel.familyUiState.value.listRelationships[selectedPosition].id,
            binding.edtEmailFamily.text.toString()
        )
    }
    private fun onMenuItemClickListener() = Toolbar.OnMenuItemClickListener {
        when (it.itemId) {
            R.id.btnReferral -> {
//                viewModel.navigateToAddAddress()
                findNavController().navigate(FamilyFragmentDirections.actionFamilyFragmentToReferralsFragment())
                true
            }

            else -> true
        }
    }

}