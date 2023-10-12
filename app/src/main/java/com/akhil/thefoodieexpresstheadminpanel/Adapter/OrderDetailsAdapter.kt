package com.akhil.thefoodieexpresstheadminpanel.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhil.thefoodieexpresstheadminpanel.databinding.OrderDetaisItemBinding
import com.bumptech.glide.Glide

class OrderDetailsAdapter(
    private var context: Context,
    private var foodNames: ArrayList<String>,
    private var foodImages: ArrayList<String>,
    private var foodQuantitys: ArrayList<Int>,
    private var foodPrices: ArrayList<String>,
) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>()
{


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder
    {
    val binding=OrderDetaisItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderDetailsViewHolder(binding)
    }



    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int)
    {
       holder.bind(position)
    }


    override fun getItemCount(): Int=foodNames.size


   inner class OrderDetailsViewHolder(private val binding: OrderDetaisItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int)
        {
         binding.apply {
             foodName.text=foodNames[position]
             foodQuantity.text=foodQuantitys[position].toString()
             val uriString=foodImages[position]
             val uri=Uri.parse(uriString)
             Glide.with(context).load(uri).into(itemFoodImage)
            foodPrice.text=foodPrices[position]
         }
        }

    }

}