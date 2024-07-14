package com.anaraya.anaraya.screens.more.language

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anaraya.anaraya.databinding.FragmentLanguageMoreBinding
import com.anaraya.anaraya.util.setLocal
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageMoreFragment : Fragment() {
    private lateinit var binding: FragmentLanguageMoreBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private var lang = ""
    private var pos = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLanguageMoreBinding.inflate(layoutInflater)

        when (sharedPreferences.getString("lang", "en")) {
            "ar" -> {
                binding.checkboxArabic.isChecked = true
                pos = 1
            }
            "en" -> {
                binding.checkboxEnglish.isChecked = true
                pos = 2
            }
            "fr" -> {
                binding.checkboxFrench.isChecked = true
                pos = 3
            }
        }

//        binding.checkboxArabic.setOnCheckedChangeListener { _, isChecked ->
//            binding.checkboxEnglish.isChecked = false
//            binding.checkboxFrench.isChecked = false
//            if (isChecked) {
//                lang = "ar"
//                pos = 1
//            }
//            else if (pos == 1) {
//                // Prevent unchecking if no other checkbox is checked
//                binding.checkboxArabic.isChecked = true
//            }
//        }
//
//        binding.checkboxEnglish.setOnCheckedChangeListener { _, isChecked ->
//            binding.checkboxArabic.isChecked = false
//            binding.checkboxFrench.isChecked = false
//            if (isChecked) {
//                lang = "en"
//                pos = 2
//            }
//            else if (pos == 2) {
//                // Prevent unchecking if no other checkbox is checked
//                binding.checkboxEnglish.isChecked = true
//            }
//        }
//
//        binding.checkboxFrench.setOnCheckedChangeListener { _, isChecked ->
//            binding.checkboxArabic.isChecked = false
//            binding.checkboxEnglish.isChecked = false
//            if (isChecked) {
//                lang = "fr"
//                pos = 3
//            }
//            else if (pos == 3) {
//                // Prevent unchecking if no other checkbox is checked
//                binding.checkboxFrench.isChecked = true
//            }
//        }

        binding.checkboxArabic.setOnClickListener {
            if(pos != 1){
                binding.checkboxEnglish.isChecked = false
                binding.checkboxFrench.isChecked = false
                lang = "ar"
                pos = 1
            }
        }
        binding.checkboxEnglish.setOnClickListener {
            if(pos != 2){
                binding.checkboxArabic.isChecked = false
                binding.checkboxFrench.isChecked = false
                lang = "en"
                pos = 2
            }
        }
        binding.checkboxFrench.setOnClickListener {
            if(pos != 3){
                binding.checkboxArabic.isChecked = false
                binding.checkboxEnglish.isChecked = false
                lang = "fr"
                pos = 3
            }
        }

        binding.btnSubmitLanguage.setOnClickListener {
            sharedPreferences.edit().putString("lang", lang).apply()
            setLocal(requireActivity(), lang)
            requireActivity().recreate()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        showBottomNavBar(requireActivity(), false)
        showToolBar(requireActivity(), true)
        showCardHome(requireActivity(),false)
    }
}