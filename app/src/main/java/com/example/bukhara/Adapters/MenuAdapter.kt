package com.example.bukhara.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bukhara.DetailsActivity
import com.example.bukhara.databinding.MenuBinding
import com.example.bukhara.model.MenuItemsModel

class MenuAdapter(
    private val menuItems: List<MenuItemsModel>, private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)

    }

    override fun getItemCount(): Int = menuItems.size


    inner class MenuViewHolder(private val binding: MenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailActivity(position)

                }


            }
        }

        private fun openDetailActivity(position: Int) {
            val menuItem = menuItems[position]

//            intent to open detailActivity and pass data
            val intent = Intent(requireContext, DetailsActivity::class.java).apply {
                putExtra("menuItemName", menuItem.foodName)
                putExtra("menuItemPrice", menuItem.foodPrice)
                putExtra("menuItemImage", menuItem.foodImage)
                putExtra("menuItemDescription", menuItem.foodDescription)
                putExtra("menuItemIngredient", menuItem.foodIngredient)
            }
            requireContext.startActivity(intent)
        }
//        set data in recycler view items

        fun bind(position: Int) {
            val menuItem = menuItems[position]

            binding.apply {
                tvFoodNameMenu.text = menuItem.foodName
                tvFoodPriceMenu.text = menuItem.foodPrice
               val uri = Uri.parse(menuItem.foodImage)
                Glide.with(requireContext).load(uri).into(ivFoodMenu)


            }
        }


    }



}


