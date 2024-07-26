package com.anaraya.anaraya.lang

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentLanguageBinding
import com.anaraya.anaraya.util.setLocal
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageFragment : Fragment() {

    private lateinit var binding : FragmentLanguageBinding
    private var index = 0
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLanguageBinding.inflate(layoutInflater)

        binding.picker.minValue = 0
        binding.picker.maxValue = 2
        val list = resources.getStringArray(R.array.lang)
        binding.picker.displayedValues = list

        binding.picker.setOnValueChangedListener { _, _, newVal ->
            index = newVal
        }

        binding.btnNext.setOnClickListener {
            when(index){
                0 -> {
                    sharedPreferences.edit().putString("lang","ar").apply()
                    setLocal(requireActivity(), "ar")
                }
                1 -> {
                    sharedPreferences.edit().putString("lang","en").apply()
                    setLocal(requireActivity(), "en")
                }
                2 -> {
                    sharedPreferences.edit().putString("lang","fr").apply()
                    setLocal(requireActivity(), "fr")
                }
            }
            requireActivity().recreate()
            findNavController().navigate(LanguageFragmentDirections.actionLanguageFragmentToAuthenticationFragment())
        }
//        binding.picker.setOnClickListener {
//        }
        return binding.root
    }

}