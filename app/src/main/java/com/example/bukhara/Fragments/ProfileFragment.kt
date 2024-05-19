package com.example.bukhara.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bukhara.R
import com.example.bukhara.databinding.FragmentProfileBinding
import com.example.bukhara.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val auth  = FirebaseAuth.getInstance()
    private val database  = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
    binding  = FragmentProfileBinding.inflate(inflater,container,false)

        setUserData()

        binding.btnSaveInfo.setOnClickListener {
            val name  = binding.etName.text.toString()
            val address  = binding.etAddress.text.toString()
            val email  = binding.etEmail.text.toString()
            val phone  = binding.etPhone.text.toString()

            updateUserData(name,address,email,phone)
        }
        binding.apply {
            etName.isEnabled = false
            etAddress.isEnabled = false
            etEmail.isEnabled = false
            etPhone.isEnabled = false
        binding.editButton.setOnClickListener {

                etName.isEnabled = !etName.isEnabled
                etAddress.isEnabled = !etAddress.isEnabled
                etEmail.isEnabled = !etEmail.isEnabled
                etPhone.isEnabled = !etPhone.isEnabled
            }
        }
        return binding.root
    }

    private fun updateUserData(name: String, address: String, email: String, phone: String) {
        val userId  = auth.currentUser?.uid
        if (userId != null){
            val userRef = database.getReference("user").child(userId)
            val userData  = hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )
            userRef.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Profile update successfully",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(requireContext(),"Profile update failed",Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun setUserData() {
        val userId  = auth.currentUser?.uid
        if (userId != null){
            val userRef  = database.getReference("user").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val userProfile  = snapshot.getValue(UserModel::class.java)
                        if (userProfile != null){
                            binding.etName.setText(userProfile.name)
                            binding.etAddress.setText(userProfile.address)
                            binding.etEmail.setText(userProfile.email)
                            binding.etPhone.setText(userProfile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }


}