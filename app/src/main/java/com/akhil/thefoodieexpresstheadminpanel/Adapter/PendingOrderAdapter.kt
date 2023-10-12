package com.akhil.thefoodieexpresstheadminpanel.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.akhil.thefoodieexpresstheadminpanel.databinding.PendingOrderItemBinding
import com.bumptech.glide.Glide

class PendingOrderAdapter(
    private var context: Context,
    private val customerNames: MutableList<String>,
    private val Quantity: MutableList<String>,
    private val foodImages: MutableList<String>,
    private val itemClicked: OnItemClicked,
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>()
{
    interface OnItemClicked
    {
        fun onItemClickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder
    {
        val binding=
            PendingOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int)
    {
        holder.bind(position)
    }

    override fun getItemCount(): Int=customerNames.size


    inner class PendingOrderViewHolder(private val binding: PendingOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root)
    {
        private var isAccepted=false
        fun bind(position: Int)
        {
            binding.apply {
                CustomerName.text=customerNames[position]
                itemQuantity.text=Quantity[position]
                var uriString=foodImages[position]
                var uri=Uri.parse(uriString)
                Glide.with(context).load(uri).into(itemFoodImage)
                buttonCondition.apply {
                    if (!isAccepted)
                    {
                        text="Accept"
                    } else
                    {
                        text="Dispatch"
                    }
                    setOnClickListener {
                        if (!isAccepted)
                        {
                            text="Dispatch"
                            isAccepted=true
                            showToast("Order is Accepted")
                            itemClicked.onItemAcceptClickListener(position)

                        } else
                        {

                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is Dispatch")
                            itemClicked.onItemDispatchClickListener(position)
                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClickListener(position)
                }
            }

        }

        private fun showToast(message: String)
        {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

    }
}
