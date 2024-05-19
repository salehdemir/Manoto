package com.example.bukhara.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bukhara.DetailsActivity
import com.example.bukhara.databinding.StablefoodBinding

class StableFoodAdapter(private val items:List<String>, private val prices: List<String>,
                        private val images: List<Int>,private val requireContext: Context):
    RecyclerView.Adapter<StableFoodAdapter.StableFoodViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StableFoodViewHolder {
       return StableFoodViewHolder(StablefoodBinding.inflate(LayoutInflater.from(parent.context)
           ,parent,false))
    }
//   just for test without binding
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StableFoodViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.your_layout_file, parent, false)
//        return StableFoodViewHolder(view)
//    }

    override fun onBindViewHolder(holder: StableFoodViewHolder, position: Int) {
       val item = items[position]
        val price = prices[position]
        val image = images[position]
        holder.bind(item,price,image)

        holder.itemView.setOnClickListener{
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("MenuItem", items[position])
            intent.putExtra("MenuImages", images[position])
            requireContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return items.size
    }
    class StableFoodViewHolder (private val binding: StablefoodBinding):RecyclerView.ViewHolder(binding.root) {
//       private val imageView=binding.ivStableFood
        fun bind(item: String,  price: String,image: Int,) {
          binding.tvFoodName.text = item
            binding.tvFoodPrice.text = price
            binding.ivStableFood.setImageResource(image)
        }

    }
}

// 2:25 repeat
