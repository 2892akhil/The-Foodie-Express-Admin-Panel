package com.akhil.thefoodieexpresstheadminpanel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhil.thefoodieexpresstheadminpanel.Adapter.PendingOrderAdapter
import com.akhil.thefoodieexpresstheadminpanel.Model.OrderDetails
import com.akhil.thefoodieexpresstheadminpanel.databinding.ActivityPendingOrderBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity(),PendingOrderAdapter.OnItemClicked
{
    private val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfTotalPrice: MutableList<String> = mutableListOf()
    private var lisetOfImageFirstFoodOrder: MutableList<String> = mutableListOf()
    private var listOfAddress:MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // initialize the database
        database=FirebaseDatabase.getInstance()

        databaseOrderDetails=database.reference.child("OrderDetails")

        getOrdersDetails()

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun getOrdersDetails()
    {
        // retrieve order details from firebase database
        databaseOrderDetails.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                for (orderSnapshot in snapshot.children){
                    val orderDetails=orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError)
            {

            }

        })
    }

    private fun addDataToListForRecyclerView()
    {
        for (orderItem in listOfOrderItem){
            // add data to respective list for populating the recyclerView
            orderItem.userName?.let { listOfName.add(it) }
            orderItem.totalPrice?.let { listOfTotalPrice.add(it) }
            orderItem.foodImages?.filterNot { it.isEmpty() }?.forEach {
                lisetOfImageFirstFoodOrder.add(it)
            }

        }
        setAdapter()
    }

    private fun setAdapter()
    {
        binding.pendingRecyclerView.layoutManager=LinearLayoutManager(this)
        val adapter=PendingOrderAdapter(this,listOfName,listOfTotalPrice,lisetOfImageFirstFoodOrder,this)
        binding.pendingRecyclerView.adapter=adapter
    }

    override fun onItemClickListener(position: Int)
    {
        val intent=Intent(this,OrderDetailsActivity::class.java)
        val userOrderDetails=listOfOrderItem[position]
        intent.putExtra("UserOrderDetails",userOrderDetails)
        startActivity(intent)

    }

    override fun onItemAcceptClickListener(position: Int)
    {
        // handle item acceptance and update database

        val childItemPushKey=listOfOrderItem[position].itemPushKey
        val clickItemOrderRefrence=childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderRefrence?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptStatus(position)
    }

    override fun onItemDispatchClickListener(position: Int)
    {
        // handle item acceptance and update database

        val dispatchItemPushKey=listOfOrderItem[position].itemPushKey
        val dispatchItemOrderReference=
            database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemFromOrderDetails(dispatchItemPushKey)
            }
    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String)
    {
        val orderDetailsItemReference=
            database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Order is Dispatched", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Order is Not Dispatched", Toast.LENGTH_SHORT).show()
            }

    }

    private fun updateOrderAcceptStatus(position: Int)
    {
// update orderAcceptance in user's buy history and order details
        val userIdOfClickedItem=listOfOrderItem[position].userUid
        val pushKeyOfClickedItem=listOfOrderItem[position].itemPushKey
        val buyHistoryReference=
            database.reference.child("user").child(userIdOfClickedItem!!).child("BuyHistory")
                .child(pushKeyOfClickedItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)
    }
}