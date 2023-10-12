package com.akhil.thefoodieexpresstheadminpanel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhil.thefoodieexpresstheadminpanel.Adapter.DeliveryAdapter
import com.akhil.thefoodieexpresstheadminpanel.Model.OrderDetails
import com.akhil.thefoodieexpresstheadminpanel.databinding.ActivityAddItemBinding
import com.akhil.thefoodieexpresstheadminpanel.databinding.ActivityOutFoodDeliveryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutFoodDeliveryActivity : AppCompatActivity()
{
    private val binding: ActivityOutFoodDeliveryBinding by lazy {
        ActivityOutFoodDeliveryBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private var listOfCompleteOrderList: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            val intent=Intent(this, MainActivity::class.java)
            finish()
        }
        // retrieve and display completed order
        retrieveCompleteOrderDetails()


    }

    private fun retrieveCompleteOrderDetails()
    {
        // initialize firebase database
        database=FirebaseDatabase.getInstance()
        val completeOrderReference=database.reference.child("CompletedOrder")
            .orderByChild("currentTime")
        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                listOfCompleteOrderList.clear()


                for (orderSnapshot in snapshot.children)
                {
                    val completeOrder=orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it)
                    }


                }
                // revers the list for display the latest order first
                listOfCompleteOrderList.reverse()

                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError)
            {

            }

        })
    }

    private fun setDataIntoRecyclerView()
    {
        // initialization list to hold Customer name and status

        val customerName= mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()


        for (order in listOfCompleteOrderList){
            order.userName?.let {
                customerName.add(it)
            }
            moneyStatus.add(order.paymentReceived)
        }
        val adapter=DeliveryAdapter(customerName,moneyStatus)
        binding.outForDeliveryRecyclerView.adapter=adapter
        binding.outForDeliveryRecyclerView.layoutManager=LinearLayoutManager(this)


    }
}