package com.example.bukhara.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlin.collections.MutableList

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.bukhara.Adapters.MenuAdapter
import com.example.bukhara.Adapters.StableFoodAdapter
import com.example.bukhara.R
import com.example.bukhara.databinding.FragmentHomeBinding
import com.example.bukhara.model.MenuItemsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
private lateinit var database: FirebaseDatabase
private lateinit var menuItems: MutableList<MenuItemsModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.tvMenu.setOnClickListener {
            val bottomSheet = MenuBottomSheetFragment()
            bottomSheet.show(parentFragmentManager,"Test")
        }
        retrieveAndDisplayStableItem()

        return binding.root



    }

    private fun retrieveAndDisplayStableItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

//        Fetch data from dataBase
                foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
//             clear existing data before populating
//                menuItems.clear()

                        for (foodSnapshot in snapshot.children){
                            val menuItem = foodSnapshot.getValue(MenuItemsModel::class.java)
                            menuItem?.let {
                                menuItems.add(it)
                            }
                        }
                        randomStableItems()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("DatabaseError","Error: ${error.message}")
                    }
                })

    }

    private fun randomStableItems() {
val index = menuItems.indices.toList().shuffled()
        val numItemShow = 6
        val menuItemsSet = index.take(numItemShow).map { menuItems[it] }
        setStableItemAdapter(menuItemsSet)
    }

    private fun setStableItemAdapter(menuItemsSet:List<MenuItemsModel>) {
        val adapter = MenuAdapter(menuItemsSet,requireContext())
        binding.recyclerStableFood.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerStableFood.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.afg1,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.afg2,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.afg3,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.afg4,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.afg5,ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList,ScaleTypes.FIT)

        imageSlider.setItemClickListener(object :ItemClickListener{
            override fun doubleClick(position: Int) {

            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "selected Image $position"
                Toast.makeText(context,itemMessage,Toast.LENGTH_SHORT).show()
            }
        })
    }
}