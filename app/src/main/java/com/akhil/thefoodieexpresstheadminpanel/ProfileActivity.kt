package com.akhil.thefoodieexpresstheadminpanel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.akhil.thefoodieexpresstheadminpanel.Model.UserModel
import com.akhil.thefoodieexpresstheadminpanel.databinding.ActivityLoginBinding
import com.akhil.thefoodieexpresstheadminpanel.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity()
{
    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        adminReference=database.reference.child("userAdmin")


        binding.backButton.setOnClickListener {
            finish()
        }

        binding.saveInformation.setOnClickListener {
            updateUserData()
        }

        binding.name.isEnabled=false
        binding.addressss3.isEnabled=false
        binding.email.isEnabled=false
        binding.phone.isEnabled=false
        binding.password.isEnabled=false
        binding.saveInformation.isEnabled=false
        var isEnable=false
        binding.editButton.setOnClickListener {
            isEnable=!isEnable
            binding.name.isEnabled=isEnable
            binding.addressss3.isEnabled=isEnable
            binding.email.isEnabled=isEnable
            binding.phone.isEnabled=isEnable
            binding.password.isEnabled=isEnable
            binding.saveInformation.isEnabled=isEnable
            if (isEnable)
            {
                binding.name.requestFocus()
            }
        }
        retrieveUserData()

    }



    private fun retrieveUserData()
    {
        val currentUserUid=auth.currentUser?.uid
        if (currentUserUid != null)
        {
            val userReference=adminReference.child(currentUserUid)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot)
                {
                    if (snapshot.exists())
                    {
                        var ownerName=snapshot.child("name").getValue()
                        var email=snapshot.child("email").getValue()
                        var password=snapshot.child("password").getValue()
                        var phone=snapshot.child("phone").getValue()
                        var address=snapshot.child("address").getValue()
                        setDataToTextview(ownerName, email, password, phone, address)
                    }

                }

                override fun onCancelled(error: DatabaseError)
                {
                    TODO("Not yet implemented")
                }

            })
        }

    }

    private fun setDataToTextview(
        ownerName: Any?,
        email: Any?,
        password: Any?,
        phone: Any?,
        address: Any?,
    )
    {
        binding.name.setText(ownerName.toString())
        binding.email.setText(email.toString())
        binding.phone.setText(phone.toString())
        binding.addressss3.setText(address.toString())
        binding.password.setText(password.toString())
    }
    private fun updateUserData()
    {
      val updateName= binding.name.text.toString()
      val updateEmail=  binding.email.text.toString()
      val updatePhone=  binding.phone.text.toString()
       val updateAddress= binding.addressss3.text.toString()
        val updatePassword=binding.password.text.toString()
        val currentUserUid=auth.currentUser?.uid
        if (currentUserUid!=null){
            val userReference=adminReference.child(currentUserUid)
            userReference.child("name").setValue(updateName)
            userReference.child("address").setValue(updateAddress)
            userReference.child("email").setValue(updateEmail)
            userReference.child("phone").setValue(updatePhone)
            userReference.child("password").setValue(updatePassword)

            Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)
        } else {
            Toast.makeText(this, "Profile Update Failed", Toast.LENGTH_SHORT).show()
        }
    }
}