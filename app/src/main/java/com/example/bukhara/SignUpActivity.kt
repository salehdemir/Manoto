package com.example.bukhara

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.bukhara.databinding.ActivitySignUpBinding
import com.example.bukhara.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {

    private lateinit var userName:String
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var auth: FirebaseAuth
    private lateinit var dataBase: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding:ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        auth = Firebase.auth
        dataBase = Firebase.database.reference
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)


        binding.btnSignSignup.setOnClickListener {
            userName = binding.signName.text.toString()
            email = binding.signEmail.text.toString().trim()
            password = binding.signPassword.text.toString().trim()

            if (userName.isBlank()|| email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Please fill all blanks" , Toast.LENGTH_SHORT).show()

            }else{
                createAccount(email,password)
            }
        }


        binding.tvAlreadyhaveaccount.setOnClickListener{
            val intent = Intent(this,LogInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSignGoogle.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            launcher.launch(intent)

        }
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account : GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener { authTask  ->
                    if (authTask.isSuccessful){
                        Toast.makeText(this,"successfully sign in with google" , Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this,"sign in failed" , Toast.LENGTH_SHORT).show()

                    }
                }
            }else{
                Toast.makeText(this,"sign in failed" , Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task ->
            if (task.isSuccessful){
                Toast.makeText(this,"Account Created Successfully" ,Toast.LENGTH_SHORT).show()
                saveUserData()
              startActivity(Intent(this,LogInActivity::class.java))
                finish()
            }else{
                Toast.makeText(this,"Account Creation Failed" ,Toast.LENGTH_SHORT).show()
                Log.d("Account","createAccount: Failure",task.exception)
            }

        }
    }

    private fun saveUserData() {
        userName = binding.signName.text.toString()
        email = binding.signEmail.text.toString().trim()
        password = binding.signPassword.text.toString().trim()
        val user= UserModel(userName,email,password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        dataBase.child("user").child(userId).setValue(user)
    }
}