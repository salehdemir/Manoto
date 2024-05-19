package com.example.bukhara.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bukhara.databinding.RecentbuyBinding

class BuyAgainAdapter(
    private val recentFoodName:MutableList<String>
    ,private val recentFoodPrice:MutableList<String>,
private val recentFoodImage:MutableList<String>,
    private var requireContext :Context
    ):RecyclerView.Adapter<BuyAgainAdapter.RecentBuyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentBuyViewHolder {
       val binding = RecentbuyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecentBuyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentBuyViewHolder, position: Int) {
     holder.bind(recentFoodName[position],recentFoodPrice[position],recentFoodImage[position])
    }

    override fun getItemCount(): Int = recentFoodName.size



  inner  class RecentBuyViewHolder(private val binding: RecentbuyBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(foodName: String, foodPrice: String, foodImage: String) {
binding.tvFoodNameHistoryAdapter.text = foodName
binding.tvFoodPriceHistoryAdapter.text = foodPrice
            val image = foodImage
            val uri = Uri.parse(image)
            Glide.with(requireContext).load(uri).into(binding.ivHistoryImageAdapter)

        }


    }
}