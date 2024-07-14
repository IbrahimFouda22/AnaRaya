package com.anaraya.anaraya.screens.more.my_information.edit_gender

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentEditGenderBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EditGenderFragment : Fragment() {
    private val navArgs by navArgs<EditGenderFragmentArgs>()
    @Inject
    lateinit var factory: EditGenderViewModel.EditGenderAssistedFactory
    private val sharedViewModel by activityViewModels<HomeActivityViewModel>()
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val viewModel by viewModels<EditGenderViewModel>{
        EditGenderViewModel.create(factory,navArgs.data.toString().toInt())
    }
    private lateinit var binding: FragmentEditGenderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditGenderBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        lifecycleScope.launch {
            viewModel.editInfoUiState.collectLatest {
                if(!it.msgUpdate.isNullOrEmpty()) {
                    if (it.isSucceed){
                        Toast.makeText(context, it.msgUpdate, Toast.LENGTH_SHORT).show()
                        sharedViewModel.getUserMyInfo()
                        sharedViewModel.getUserMoreInfo()
                    }
                }
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.valueState.collectLatest {
                viewModel.updateVisibility()
            }
        }

        binding.radioGroupGenderEditInfo.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.radioMaleEditInfo -> {
                    viewModel.updateGender(1)
                }
                R.id.radioFemaleEditInfo -> {
                    viewModel.updateGender(2)
                }
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
    private fun reload() {
        sharedViewModel.reloadClick()
        showToolBar(requireActivity(), true)
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    override fun onStart() {
        super.onStart()
        showToolBar(requireActivity(),true)
    }

}