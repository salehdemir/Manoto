package com.example.bukhara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bukhara.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private  val binding: ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnStartNext.setOnClickListener{
            val intent = Intent(this,LogInActivity::class.java)
            startActivity(intent)
        }


    }
}