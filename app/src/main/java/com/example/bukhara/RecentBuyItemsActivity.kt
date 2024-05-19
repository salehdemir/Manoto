package com.example.bukhara

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bukhara.Adapters.RecentBuyAdapter
import com.example.bukhara.databinding.ActivityRecentBuyItemsBinding
import com.example.bukhara.model.OrderDetailsModel

class RecentBuyItemsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecentBuyItemsBinding

    private lateinit var allFoodNames:ArrayList<String>
    private lateinit var allFoodPrices:ArrayList<String>
    private lateinit var allFoodImages:ArrayList<String>
    private lateinit var allFoodQuantity:ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecentBuyItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener{
            finish()
        }

        val recentOrderItems = intent.getSerializableExtra("RecentBuyItems") as ArrayList<OrderDetailsModel>
        recentOrderItems?.let { orderDetails ->
            if (orderDetails.isNotEmpty()){
                val recentOrderItem =orderDetails[0]

                allFoodNames = recentOrderItem.foodName as ArrayList<String>
                allFoodPrices = recentOrderItem.foodPrice as ArrayList<String>
                allFoodImages = recentOrderItem.foodImage as ArrayList<String>
                allFoodQuantity = recentOrderItem.foodQuantity as ArrayList<Int>
            }
        }
        setAdapter()

    }

    private fun setAdapter() {
        val rv = binding.recentItemRecyclerView
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(this,allFoodNames,allFoodPrices,allFoodImages,allFoodQuantity)
    rv.adapter = adapter
    }
}