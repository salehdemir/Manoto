package com.example.bukhara.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.example.bukhara.databinding.CartitemsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.net.URI

class CartAdapter(
    private val context : Context,
    private val cartItems: MutableList<String>,
    private val cartPrices: MutableList<String>,
    private val cartImages: MutableList<String>,
    private val cartDescription :MutableList<String>,
    private val cartIngredients:MutableList<String>,
    private val cartQuantity:MutableList<Int>,

) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

        private val auth = FirebaseAuth.getInstance()

    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        val cartItemNum = cartItems.size

        cartItemQuantity = IntArray(cartItemNum){1}
        cartItemReference =database.reference.child("user").child(userId).child("cartItems")
    }
    companion object{
        private var cartItemQuantity :IntArray= intArrayOf()
        private lateinit var cartItemReference: DatabaseReference
    }

//    private val cartItemQuantity = IntArray(cartItems.size) { 1 }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartitemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    fun getUpdateItemsQuantities(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(cartQuantity)
        return itemQuantity

    }

    inner class CartViewHolder(private val binding: CartitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = cartItemQuantity[position]
                tvFoodNameCart.text = cartItems[position]
                tvFoodPriceCart.text = cartPrices[position]
//                ivCartImage.setImageResource(cartImages[position])
//                load image using glide
                val uriString = cartImages[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(ivCartImage)
                tvCartQuantity.text = quantity.toString()

                cartMinus.setOnClickListener {
                    decreaseQuantity(position)
                }
                cartPlus.setOnClickListener {
                    increaseQuantity(position)

                }
                cartDeleteIcon.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(itemPosition)
                    }


                }
            }
        }

        private fun increaseQuantity(position: Int) {
            if (cartItemQuantity[position] < 10) {
                cartItemQuantity[position]++
                cartQuantity[position] = cartItemQuantity[position]
                binding.tvCartQuantity.text = cartItemQuantity[position].toString()
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (cartItemQuantity[position] > 1) {
                cartItemQuantity[position]--
                cartQuantity[position] = cartItemQuantity[position]
                binding.tvCartQuantity.text = cartItemQuantity[position].toString()
            }
        }

        private fun deleteItem(position: Int) {
            val positionRetrieve = position
            getUniqueKeyForPosition(positionRetrieve){uniqueKey ->
                if (uniqueKey != null){
removeItem(position,uniqueKey)
                }
            }
//            cartItems.removeAt(position)
//            cartPrices.removeAt(position)
//            cartImages.removeAt(position)
//            notifyItemRemoved(position)
//            notifyItemRangeChanged(position, cartItems.size)

        }

        private fun removeItem(position: Int, uniqueKey: String) {
            if (uniqueKey != null){
                cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    cartItems.removeAt(position)
                    cartPrices.removeAt(position)
                    cartImages.removeAt(position)
                    cartDescription.removeAt(position)
                    cartIngredients.removeAt(position)
                    cartQuantity.removeAt(position)

                    Toast.makeText(context,"Item deleted", Toast.LENGTH_SHORT).show()

//                    update Item Quantity
                    cartItemQuantity = cartItemQuantity.filterIndexed { index, i -> index!= position }.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position,cartItems.size)
                }.addOnFailureListener {
                    Toast.makeText(context,"Failed to delete", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getUniqueKeyForPosition(positionRetrieve: Int,onComplete:(String?) -> Unit  ) {
            cartItemReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null

                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == positionRetrieve){
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}