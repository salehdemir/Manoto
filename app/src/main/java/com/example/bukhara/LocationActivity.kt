package com.example.bukhara

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import com.example.bukhara.databinding.ActivityLocationBinding
import com.example.bukhara.databinding.ActivitySignUpBinding

class LocationActivity : AppCompatActivity() {
    private val binding:ActivityLocationBinding by lazy{
        ActivityLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val locationList =arrayOf("District-1","District-2","District-3","District-4","District-5","District-6","District-7","District-8","District-9","District-10","District-11","District-12")
val adapter =ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)
    }
}