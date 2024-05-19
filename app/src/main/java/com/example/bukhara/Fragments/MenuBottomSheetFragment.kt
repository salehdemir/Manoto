package com.example.bukhara.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bukhara.Adapters.MenuAdapter

import com.example.bukhara.databinding.FragmentMenuBottomSheetBinding
import com.example.bukhara.model.MenuItemsModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MenuBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var menuItems: MutableList<MenuItemsModel>
    private lateinit var database:FirebaseDatabase
    private lateinit var binding:FragmentMenuBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBottomSheetBinding.inflate(inflater,container,false)

        binding.buttonBack.setOnClickListener {
          dismiss()
        }

       retrieveMenuItems()


        return binding.root
    }

    private fun retrieveMenuItems() {
        database =FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference =database.reference.child("menu")
menuItems = mutableListOf( )
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
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError","Error: ${error.message}")
            }
        })
    }
    private fun setAdapter() {

        val adapter = MenuAdapter(
            menuItems,requireContext()
        )

        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
    }
//


}
