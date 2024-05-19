package com.example.bukhara

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.bukhara.databinding.ActivityDetailsBinding
import com.example.bukhara.model.CartItemsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private  var foodName:String?  = null
    private  var foodPrice:String?  = null
    private  var foodDescription:String?  = null
    private  var foodIngredient:String? = null
    private var foodImage: String?  = null
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        foodName = intent.getStringExtra("menuItemName")
        foodPrice = intent.getStringExtra("menuItemPrice")
        foodDescription = intent.getStringExtra("menuItemDescription")
        foodIngredient = intent.getStringExtra("menuItemIngredient")
        foodImage = intent.getStringExtra("menuItemImage")

        with(binding){
            tvFoodNameDetail.text = foodName
            tvDesDetail.text = foodDescription
            tvingredientsDetails.text = foodIngredient
           Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(ivDetail)
        }

        binding.buttonBack.setOnClickListener{
            finish()
        }
        binding.btnAddtoCart.setOnClickListener {
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""

//        Create a Cart Item Object
        val cartItem = CartItemsModel(foodName.toString(),foodPrice.toString(),foodImage.toString(),foodDescription.toString(),foodIngredient,1)

//        save data to cart item to firebase database
        database.child("user").child(userId).child("cartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this,"Items added to cart successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this,"Items Not added", Toast.LENGTH_SHORT).show()

        }
    }
}