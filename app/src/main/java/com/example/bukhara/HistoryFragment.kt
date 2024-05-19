package com.example.bukhara

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.bukhara.Adapters.BuyAgainAdapter
import com.example.bukhara.databinding.FragmentHistoryBinding
import com.example.bukhara.model.OrderDetailsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth : FirebaseAuth
    private lateinit var userId :String
    private var listOfOrderItem :ArrayList<OrderDetailsModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

//        Retrieve and display user order history
        retrieveBuyHistory()
        binding.recentlyBuyItem.setOnClickListener{
            recentItemsBuy()
        }

        return binding.root
    }

    private fun recentItemsBuy() {
        listOfOrderItem.firstOrNull()?.let { recentBuy ->
            val intent = Intent(requireContext(),RecentBuyItemsActivity::class.java)
            intent.putExtra("RecentBuyItems",listOfOrderItem )
            startActivity(intent)
        }
    }

    private fun retrieveBuyHistory() {
binding.recentlyBuyItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid?:""

        val buyItemRef :DatabaseReference = database.reference.child("user").child(userId).child("BuyHistory")
        val shortQuery = buyItemRef.orderByChild("currentTime")

        shortQuery.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children){
                    val buyHistoryItem = buySnapshot.getValue(OrderDetailsModel::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()){
                    setDataInRecentBuyItem()
                    setPreviousBuyItemRecyclerView()
                }
            }



            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun setDataInRecentBuyItem() {
        binding.recentlyBuyItem.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItem.firstOrNull()
        recentOrderItem?.let {
            with(binding){
                tvFoodNameHistory.text = it.foodName?.firstOrNull()?:""
                tvFoodPriceHistory.text = it.foodPrice?.firstOrNull()?:""
                val image = it.foodImage?.firstOrNull()?:""
                val uri = Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(ivHistoryImage)

                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()){

                }
            }
        }
    }

    private fun setPreviousBuyItemRecyclerView() {
        val recentFoodName = mutableListOf<String>()
        val recentFoodPrice = mutableListOf<String>()
        val recentFoodImage = mutableListOf<String>()
        for (i in 1 until listOfOrderItem.size) {
            listOfOrderItem[i].foodName?.firstOrNull()?.let {
                recentFoodName.add(it)
                listOfOrderItem[i].foodPrice?.firstOrNull()?.let {
                    recentFoodPrice.add(it)
                    listOfOrderItem[i].foodImage?.firstOrNull()?.let {
                        recentFoodImage.add(it)
                    }
                }
                val recyclerView = binding.historyRecyclerView
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                buyAgainAdapter = BuyAgainAdapter(
                    recentFoodName,
                    recentFoodPrice,
                    recentFoodImage,
                    requireContext()
                )
                recyclerView.adapter = buyAgainAdapter

            }
        }
        }
    }
