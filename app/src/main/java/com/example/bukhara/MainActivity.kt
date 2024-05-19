package com.example.bukhara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bukhara.Fragments.MenuBottomSheetFragment
import com.example.bukhara.Fragments.NotificationBottomSheet
import com.example.bukhara.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notificationIcon.setOnClickListener{
            val bottomSheet = NotificationBottomSheet()
            bottomSheet.show(supportFragmentManager,"Test")
        }

        var navController: NavController = findNavController(R.id.fragmentContainerView)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigation.setupWithNavController(navController)
    }
}