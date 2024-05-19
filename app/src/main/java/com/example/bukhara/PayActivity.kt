package com.example.bukhara

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bukhara.databinding.ActivityPayBinding
import com.example.bukhara.model.OrderDetailsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var payName: String
    private lateinit var payAddress: String
    private lateinit var payPhone: String
    private lateinit var payTotalAmount: String
    private lateinit var foodItemName:ArrayList <String>
    private lateinit var foodItemPrice:ArrayList <String>
    private lateinit var foodItemImage: ArrayList <String>
    private lateinit var foodItemDescription:ArrayList <String>
    private lateinit var foodItemIngredient: ArrayList <String>
    private lateinit var foodItemQuantity: ArrayList <Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()

        setUserData()

        val intent  = intent
        foodItemName =intent.getStringArrayListExtra("FoodItemName") ?: ArrayList()
        foodItemPrice =intent.getStringArrayListExtra("FoodItemPrice")  ?: ArrayList()
        foodItemImage =intent.getStringArrayListExtra("FoodItemImage") ?: ArrayList()
        foodItemDescription =intent.getStringArrayListExtra("FoodItemDescription")  ?: ArrayList()
        foodItemIngredient =intent.getStringArrayListExtra("FoodItemIngredient")  ?: ArrayList()
        foodItemQuantity =intent.getIntegerArrayListExtra("FoodItemQuantity")  ?: ArrayList()

        payTotalAmount = calculateTotalAmount().toString() + " $"
//        binding.payTotalAmount.isEnabled = false
        binding.payTotalAmount.setText(payTotalAmount)


            binding.btnPlaceOrder.setOnClickListener{
                payName = binding.payName.text.toString().trim()
                payAddress = binding.etAddress.text.toString().trim()
                payPhone = binding.etPhone.text.toString().trim()

                if (payName.isBlank()&&payAddress.isBlank()&&payPhone.isBlank()) {
                    Toast.makeText(this,"Please enter all the detail",Toast.LENGTH_SHORT).show()
                }else{
                    placeOrder()
                }


        }
        binding.btnBack.setOnClickListener{
            finish()
        }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid?:""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetail = OrderDetailsModel(userId,payName,foodItemName,foodItemPrice,foodItemImage,foodItemQuantity,payAddress,payPhone,time,itemPushKey,false,false)
    val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetail).addOnSuccessListener {
            val bottomSheet = DoneBottomSheet()
            bottomSheet.show(supportFragmentManager,"Test")
            removeItemFromCard()
            addOrderToHistory(orderDetail)
        }
            .addOnFailureListener{
                Toast.makeText(this,"Failed to Order",Toast.LENGTH_SHORT).show()
            }
    }

    private fun addOrderToHistory(orderDetail: OrderDetailsModel) {
databaseReference.child("user").child(userId).child("BuyHistory")
    .child(orderDetail.itemPushKey!!)
    .setValue(orderDetail).addOnSuccessListener {

    }
    }

    private fun removeItemFromCard() {
        val cartItemReference  =  databaseReference.child("user").child(userId).child("cartItems")
        cartItemReference.removeValue()
    }


    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until foodItemPrice.size){
            var price = foodItemPrice[i]
            val lastChar = price.last()
            val priceIntValue = if (lastChar == '$'){
                price.dropLast(1).toInt()
            }else{
                price.toInt()
            }
            var quantity = foodItemQuantity[i]
            totalAmount += priceIntValue * quantity
        }
        return  totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null){
            val userId = user.uid
            val userReference =databaseReference.child("user").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val name = snapshot.child("name").getValue(String::class.java)
                        val address = snapshot.child("address").getValue(String::class.java)
                        val phone = snapshot.child("phone").getValue(String::class.java)
                        binding.apply {
                            payName.setText(name)
                            etAddress.setText(address)
                            etPhone.setText(phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}