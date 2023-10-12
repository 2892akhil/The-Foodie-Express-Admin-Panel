package com.akhil.thefoodieexpresstheadminpanel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhil.thefoodieexpresstheadminpanel.Adapter.MenuItemAdapter
import com.akhil.thefoodieexpresstheadminpanel.Model.AllMenu
import com.akhil.thefoodieexpresstheadminpanel.databinding.ActivityAllItemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity()
{
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private  var menuItems:ArrayList<AllMenu> = ArrayList()
    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        databaseReference=FirebaseDatabase.getInstance().reference
        retrieveMenuItem()
        binding.backButton.setOnClickListener {
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    private fun retrieveMenuItem()
    {
        database=FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference=database.reference.child("Menu")
        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                menuItems.clear()
                for (foodSnapshot in snapshot.children){
                    val menuItem=foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError)
            {
                Log.d(" DatabaseError", "Error:${error.message} ")
            }
        })
    }

    private fun setAdapter()
    {
        val adapter=MenuItemAdapter(this@AllItemActivity,menuItems,databaseReference){position ->
        deleteMenuItems(position)
    }
        binding.menuRecyclerView.layoutManager=LinearLayoutManager(this)
        binding.menuRecyclerView.adapter=adapter
    }

    private fun deleteMenuItems(position: Int)
    {
       val menuItemToDelete=menuItems[position]
        val menuItemKey=menuItemToDelete.key
        val foodMenuReference=database.reference.child("Menu").child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful){
                menuItems.removeAt(position)
                binding.menuRecyclerView.adapter?.notifyItemRemoved(position)
            }else{
                Toast.makeText(this, "Item Deleted Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}