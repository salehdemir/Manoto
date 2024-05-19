package com.example.bukhara.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bukhara.Adapters.CartAdapter
import com.example.bukhara.PayActivity
import com.example.bukhara.R
import com.example.bukhara.databinding.FragmentCardBinding
import com.example.bukhara.model.CartItemsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CardFragment : Fragment() {

    private lateinit var binding: FragmentCardBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodImageUri: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCardBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        retrieveCartItems()



        binding.btnProceed.setOnClickListener {

//            get order items details for check out
            getOrderItemDetails()

//            val intent = Intent(requireContext(), PayActivity::class.java)
//            startActivity(intent)
        }
        return binding.root
    }

    private fun getOrderItemDetails() {
        val orderIdReference: DatabaseReference =
            database.reference.child("user").child(userId).child("cartItems")

        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()

//        get items quantity
        val foodQuantity = cartAdapter.getUpdateItemsQuantities()

        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapShot in snapshot.children) {
                    val orderItems = foodSnapShot.getValue(CartItemsModel::class.java)
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodIngredient?.let { foodIngredient.add(it) }
                }
                orderNow(
                    foodName,
                    foodPrice,
                    foodImage,
                    foodDescription,
                    foodIngredient,
                    foodQuantity
                )
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Order making failed, Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }


        })


    }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodImage: MutableList<String>,
        foodDescription: MutableList<String>,
        foodIngredient: MutableList<String>,
        foodQuantity: MutableList<Int>
    ) {
        if (isAdded && context != null) {
            val intent = Intent(requireContext(), PayActivity::class.java)
            intent.putExtra("FoodItemName", foodName as ArrayList<String>)
            intent.putExtra("FoodItemPrice", foodPrice as ArrayList<String>)
            intent.putExtra("FoodItemImage", foodImage as ArrayList<String>)
            intent.putExtra("FoodItemDescription", foodDescription as ArrayList<String>)
            intent.putExtra("FoodItemIngredient", foodIngredient as ArrayList<String>)
            intent.putExtra("FoodItemQuantity", foodQuantity as ArrayList<Int>)
            startActivity(intent)
        }
    }

    private fun retrieveCartItems() {
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        val foodRef: DatabaseReference =
            database.reference.child("user").child(userId).child("cartItems")

//        list to store cart items
        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodImageUri = mutableListOf()
        foodDescriptions = mutableListOf()
        foodIngredients = mutableListOf()
        quantity = mutableListOf()

//        fetch the data from the database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val cartItems = foodSnapshot.getValue(CartItemsModel::class.java)
//                    add cart items details to list
                    cartItems?.foodName?.let { foodNames.add(it) }
                    cartItems?.foodPrice?.let { foodPrices.add(it) }
                    cartItems?.foodImage?.let { foodImageUri.add(it) }
                    cartItems?.foodDescription?.let { foodDescriptions.add(it) }
                    cartItems?.foodIngredient?.let { foodIngredients.add(it) }
                    cartItems?.foodQuantity?.let { quantity.add(it) }

                }
                setAdapter()
            }

            private fun setAdapter() {
              cartAdapter = CartAdapter(
                    requireContext(),
                    foodNames,
                    foodPrices,
                    foodImageUri,
                    foodDescriptions,
                    foodIngredients,
                    quantity
                )
                binding.cartRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.cartRecyclerView.adapter = cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data not fetch", Toast.LENGTH_SHORT).show()

            }
        })
    }

}