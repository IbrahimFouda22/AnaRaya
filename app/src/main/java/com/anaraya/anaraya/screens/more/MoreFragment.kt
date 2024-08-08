package com.anaraya.anaraya.screens.more

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.MainActivity
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentMoreBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.RealPathUtil
import com.anaraya.anaraya.util.gone
import com.anaraya.anaraya.util.removeUser
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MoreFragment : Fragment() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentMoreBinding
    private val viewModel by viewModels<MoreViewModel>({ this })
    private val sharedViewModel by activityViewModels<HomeActivityViewModel>()
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_allow_permission_to_upload_image),
                    Toast.LENGTH_SHORT
                ).show()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        val isFamily = sharedPreferences.getBoolean("isFamily", false)

        if (isFamily) {
            binding.imgChildMore.gone()
            binding.txtChildMore.gone()
        }
        binding.btnLogOut.setOnClickListener {
            val token = sharedPreferences.getString("fcm_token", "")
            if (token != null) {
                viewModel.sendFCMToken(
                    token = token, enabled = false, isUpdate = true
                )
            }
        }

        lifecycleScope.launch {
            viewModel.moreUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.uploadMsg != null) {
                    Toast.makeText(context, it.uploadMsg, Toast.LENGTH_SHORT).show()
                }
                if (it.navigateToFeedBack) {
                    findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToFeedBackFragment())
                    viewModel.navigateToFeedBackDone()
                }
                if (it.navigateToMyInfo) {
                    findNavController().navigate(
                        MoreFragmentDirections.actionMoreFragmentToMyInformationFragment(
                            viewModel.moreUiState.value.profileUiState
                        )
                    )
                    viewModel.navigateToMyInfoDone()
                }
                if (it.navigateToOrders) {
                    findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToOrdersFragment())
                    viewModel.navigateToOrdersDone()
                }
                if (it.navigateToHelp) {
                    findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToHelpFragment2())
                    viewModel.navigateToHelpDone()
                }
                if (it.navigateToSchedule) {
                    findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToScheduleFragment())
                    viewModel.navigateToScheduleDone()
                }
                if (it.navigateToAboutUs) {
                    findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToAboutUsFragment())
                    viewModel.navigateToAboutUsDone()
                }
                if (it.navigateToFamily) {
                    findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToFamilyFragment())
                    viewModel.navigateToFamilyDone()
                }
                if (it.navigateToTermsCondition) {
                    findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToTermsAndConditionFragment())
                    viewModel.navigateToTermsConditionDone()
                }
                if (it.navigateToLanguage) {
                    findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToLanguageMoreFragment())
                    viewModel.navigateToLanguageDone()
                }
                if (it.navigateToPrivacyPolicy) {
                    findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToPrivacyPolicyFragment())
                    viewModel.navigateToPrivacyPolicyDone()
                }
                if (it.navigateToSurveys) {
                    findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToSurveysFragment())
                    viewModel.navigateToSurveysDone()
                }
                if (it.isFCMSent) {
                    logOut()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.homeState.collectLatest {
                if (it.getUserMoreInfo) {
                    viewModel.getAllData()
                    sharedViewModel.getUserMoreDone()
                }
            }
        }
        binding.imgPersonalMore.setOnClickListener {
            checkExternalPermissionAndPickImage()
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
        showToolBar(requireActivity(), false)
        showCardHome(requireActivity(), false)
        showBottomNavBar(requireActivity(), true)
    }

    private fun logOut() {
        removeUser(sharedPreferences, requireContext())
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
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

    private fun checkExternalPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            )
                pickImageFromGallery()
            else
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            )
                pickImageFromGallery()
            else
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onActivityResult(requestCode, resultCode, data)",
            "androidx.fragment.app.Fragment"
        )
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 3) {
            val image = data?.data
            val file = File(RealPathUtil.getRealPath(requireContext(), image!!)!!)
            viewModel.uploadImage(file)
        }
    }

//    private fun openSettings(){
//        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//        val uri = Uri.fromParts("package", requireActivity().packageName, null)
//        intent.data = uri
//        startActivity(intent)
//    }

}