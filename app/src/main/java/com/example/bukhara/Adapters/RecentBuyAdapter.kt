package com.example.bukhara.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bukhara.databinding.RecentbuyBinding
import com.example.bukhara.databinding.RecentitemBinding

class RecentBuyAdapter(
    private var context: Context,
    private val foodNameList: ArrayList<String>,
    private val foodPriceList: ArrayList<String>,
    private val foodImageList: ArrayList<String>,
    private val foodQuantityList: ArrayList<Int>
) : RecyclerView.Adapter<RecentBuyAdapter.RecentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val binding = RecentitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = foodNameList.size


    inner class RecentViewHolder(private val binding: RecentitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                tvFoodNameRecent.text = foodNameList[position]
                tvFoodPriceRecent.text=foodPriceList[position]
                quantity.text = foodQuantityList[position].toString()
                val image = foodImageList[position]
                val uri = Uri.parse(image)
                Glide.with(context).load(uri).into(ivFoodRecent)

            }
        }

    }
}