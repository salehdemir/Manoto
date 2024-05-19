package com.example.bukhara

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bukhara.databinding.FragmentDoneBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DoneBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentDoneBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      binding = FragmentDoneBottomSheetBinding.inflate(inflater,container,false)
        binding.btnGohome.setOnClickListener {
           val intent = Intent(requireContext(),MainActivity::class.java)
           startActivity(intent)
        }
        return binding.root
    }


}