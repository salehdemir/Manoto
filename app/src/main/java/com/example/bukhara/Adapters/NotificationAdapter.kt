package com.example.bukhara.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bukhara.databinding.NotificationItemsBinding

class NotificationAdapter(private val NotificationItems:ArrayList<String>,
                          private val NotificationImages:ArrayList<Int> )
    :RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
     holder.bind(position)
    }

    override fun getItemCount(): Int {
     return  NotificationItems.size
    }

   inner class NotificationViewHolder (private val binding: NotificationItemsBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
               tvNotification.text = NotificationItems[position]
                ivNotification.setImageResource(NotificationImages[position])
            }
        }

    }
}