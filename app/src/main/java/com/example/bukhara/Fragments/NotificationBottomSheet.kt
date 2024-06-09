package com.example.bukhara.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bukhara.Adapters.MenuAdapter
import com.example.bukhara.Adapters.NotificationAdapter
import com.example.bukhara.R
import com.example.bukhara.databinding.FragmentNotificationBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class NotificationBottomSheet : BottomSheetDialogFragment(){

    private lateinit var binding: FragmentNotificationBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentNotificationBottomSheetBinding.inflate(layoutInflater,container,false)

        val notificationText = listOf(
            "Your order cancelled successfully",
            "Order has been taken by driver",
            "Congrats your order placed",)
        val notificationImage = listOf(
            R.drawable.iconsad,
            R.drawable.deliverybike,
            R.drawable.success)

        val adapter = NotificationAdapter(
            ArrayList ( notificationText),
            ArrayList( notificationImage),

        )
        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter = adapter

        return binding.root
    }

   
}