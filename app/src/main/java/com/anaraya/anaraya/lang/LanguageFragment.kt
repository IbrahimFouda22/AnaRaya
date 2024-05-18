package com.anaraya.anaraya.lang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentLanguageBinding
import com.anaraya.anaraya.util.setLocal

class LanguageFragment : Fragment() {

    private lateinit var binding : FragmentLanguageBinding
    private var index = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLanguageBinding.inflate(layoutInflater)

        binding.picker.minValue = 0
        binding.picker.maxValue = 1
        val list = resources.getStringArray(R.array.lang)
        binding.picker.displayedValues = list

        binding.picker.setOnValueChangedListener { _, _, newVal ->
            index = newVal
        }

        binding.picker.setOnClickListener {
            setLocal(requireActivity(),"en")
            findNavController().navigate(LanguageFragmentDirections.actionLanguageFragmentToAuthenticationFragment())
        }
        return binding.root
    }

}