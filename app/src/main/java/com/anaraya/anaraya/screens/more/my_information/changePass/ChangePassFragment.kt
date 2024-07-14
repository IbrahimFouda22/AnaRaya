package com.anaraya.anaraya.screens.more.my_information.changePass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentChangePassBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePassFragment : Fragment() {
    private val sharedViewModel by activityViewModels<HomeActivityViewModel>()
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private lateinit var binding: FragmentChangePassBinding
    private val viewModel by viewModels<ChangePassViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChangePassBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        lifecycleScope.launch {
            viewModel.editInfoUiState.collectLatest {
                if (!it.msgUpdate.isNullOrEmpty()) {
                    if (it.isSucceed) {
                        Toast.makeText(context, it.msgUpdate, Toast.LENGTH_SHORT).show()
                    }
                }
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnSaveEditInfo.setOnClickListener {
            changePass()
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
        changePass()
        sharedViewModel.reloadClickDone()
    }

    override fun onStart() {
        super.onStart()
        showToolBar(requireActivity(), true)
    }
    private fun changePass(){
        viewModel.updateData(
            binding.edtCurrentPass.text.toString(),
            binding.edtNewPass.text.toString()
        )
    }
}