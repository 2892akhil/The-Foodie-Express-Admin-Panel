package com.akhil.thefoodieexpresstheadminpanel

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akhil.thefoodieexpresstheadminpanel.Model.OrderDetails
import com.akhil.thefoodieexpresstheadminpanel.databinding.ActivityMainBinding
import com.akhil.thefoodieexpresstheadminpanel.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity()
{
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completedOrderReference: DatabaseReference
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        user=FirebaseAuth.getInstance()


        binding.AddMenuItem.setOnClickListener {
            val intent=Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
        binding.allItemMenu.setOnClickListener {
            val intent=Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }
        binding.orderDispatch.setOnClickListener {
            val intent=Intent(this, OutFoodDeliveryActivity::class.java)
            startActivity(intent)
        }
        binding.profile.setOnClickListener {
            val intent=Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.pendingOrder.setOnClickListener {
            val intent=Intent(this, PendingOrderActivity::class.java)
            startActivity(intent)
        }
        binding.logOut.setOnClickListener {
            user.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        pendingOrders()

        completedOrders()

    }



    private fun completedOrders()
    {
        val completeOrderReference=database.reference.child("CompletedOrder")
        var completeOrderItemCount=0
        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                completeOrderItemCount=snapshot.childrenCount.toInt()
                binding.totalCompleteOrder.text=completeOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError)
            {

            }
        })
    }

    private fun pendingOrders()
    {
        database=FirebaseDatabase.getInstance()
        val pendingOrderReference=database.reference.child("OrderDetails")
        var pendingOrderItemCount=0
        pendingOrderReference.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                pendingOrderItemCount=snapshot.childrenCount.toInt()
                binding.totalPendingOrder.text=pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError)
            {

            }
        })
    }
}