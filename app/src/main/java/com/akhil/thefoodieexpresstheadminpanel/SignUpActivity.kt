package com.akhil.thefoodieexpresstheadminpanel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.akhil.thefoodieexpresstheadminpanel.Model.UserModel
import com.akhil.thefoodieexpresstheadminpanel.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth
    private lateinit var emailRe: String
    private lateinit var passwordRe: String
    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var database: DatabaseReference
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth=Firebase.auth
        //initialize firebase database
        database=Firebase.database.reference
        binding.alreadyAccount.setOnClickListener {
            val intent=Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.createAccount.setOnClickListener {
            userName=binding.nameNewUser.text.toString().trim()
            nameOfRestaurant=binding.restro.text.toString().trim()
            emailRe=binding.editTextTextEmailAddress2.text.toString().trim()
            passwordRe=binding.passwordNewUser.text.toString().trim()
            address=binding.addressss2.text.toString().trim()
            phone=binding.phoneNo.text.toString().trim()
            if (userName.isBlank() || nameOfRestaurant.isBlank() || emailRe.isBlank() || passwordRe.isBlank() || address.isBlank() || phone.isBlank())
            {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_LONG).show()
            } else
            {
                createAccount(emailRe, passwordRe)
            }
        }
    }

    private fun createAccount(emailRe: String, passwordRe: String)
    {
        auth.createUserWithEmailAndPassword(emailRe, passwordRe).addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent=Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else
            {
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure", task.exception)
            }
        }
    }

    private fun saveUserData()
    {
        userName=binding.nameNewUser.text.toString().trim()
        nameOfRestaurant=binding.restro.text.toString().trim()
        emailRe=binding.editTextTextEmailAddress2.text.toString().trim()
        passwordRe=binding.passwordNewUser.text.toString().trim()
        address=binding.addressss2.text.toString().trim()
        phone=binding.phoneNo.text.toString().trim()
        val user=UserModel(userName, nameOfRestaurant, emailRe, passwordRe, address,phone)
        val userId: String=FirebaseAuth.getInstance().currentUser!!.uid
        //save user data firebase database
        database.child("userAdmin").child(userId).setValue(user)
    }
}