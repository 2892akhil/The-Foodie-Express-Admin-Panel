package com.akhil.thefoodieexpresstheadminpanel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhil.thefoodieexpresstheadminpanel.Adapter.OrderDetailsAdapter
import com.akhil.thefoodieexpresstheadminpanel.Model.OrderDetails
import com.akhil.thefoodieexpresstheadminpanel.databinding.ActivityOrderDetailsBinding

class OrderDetailsActivity : AppCompatActivity()
{
    private val binding: ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    private var username: String?=null
    private var address: String?=null
    private var phoneNumber: String?=null
    private var totalPrice: String?=null
    private  var foodNames:ArrayList<String> = arrayListOf()
    private  var foodImages: ArrayList<String> = arrayListOf()
    private var foodQuantity: ArrayList<Int> = arrayListOf()
    private  var foodPrices: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonBackEdit.setOnClickListener {
            finish()
        }
        getDataFromIntent()
    }

    private fun getDataFromIntent()
    {
        val receivedOrderDetails=intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        receivedOrderDetails?.let { orderDetails ->
            username=receivedOrderDetails.userName
            foodNames=receivedOrderDetails.foodNames as ArrayList<String>
            foodImages=receivedOrderDetails.foodImages as ArrayList<String>
            foodQuantity=receivedOrderDetails.foodQuantities as ArrayList<Int>
            foodPrices=receivedOrderDetails.foodPrices as ArrayList<String>
            address=receivedOrderDetails.address
            phoneNumber=receivedOrderDetails.phoneNumber
            totalPrice=receivedOrderDetails.totalPrice

            setUserDetails()
            setAdapter()
        }

    }



    private fun setUserDetails()
    {
        binding.name.text=username
        binding.address.text=address
        binding.phoneNo.text=phoneNumber
        binding.totalAmount.text=totalPrice
    }

    private fun setAdapter()
    {
        binding.rvAllDetails.layoutManager=LinearLayoutManager(this)
        val adapter=OrderDetailsAdapter(this, foodNames, foodImages, foodQuantity, foodPrices)
        binding.rvAllDetails.adapter=adapter
    }
}