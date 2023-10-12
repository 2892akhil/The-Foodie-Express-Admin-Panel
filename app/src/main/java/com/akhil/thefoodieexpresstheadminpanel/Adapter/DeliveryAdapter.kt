package com.akhil.thefoodieexpresstheadminpanel.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhil.thefoodieexpresstheadminpanel.databinding.DeliveryItemBinding

class DeliveryAdapter(
    private val CustomerName: MutableList<String>,
    private val MoneyStatus: MutableList<Boolean>,
) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder
    {
        val binding=DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }


    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int)
    {
        holder.bind(position)
    }

    override fun getItemCount(): Int=CustomerName.size
    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root)
    {
        fun bind(position: Int)
        {
            binding.apply {
                customerName.text=CustomerName[position]
                if (MoneyStatus[position]==true){
                    status.text="Received"
                }else{
                    status.text="Not Received"
                }
                val colorMap=mapOf(
                    true to Color.GREEN,
                    false to Color.RED
                )
                status.setTextColor(colorMap[MoneyStatus[position]] ?: Color.BLACK)
                statusButton.backgroundTintList=
                    ColorStateList.valueOf(colorMap[MoneyStatus[position]] ?: Color.BLACK)
            }
        }

    }
}