package com.akhil.thefoodieexpresstheadminpanel

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.akhil.thefoodieexpresstheadminpanel.Model.AllMenu
import com.akhil.thefoodieexpresstheadminpanel.databinding.ActivityAddItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class AddItemActivity : AppCompatActivity()
{
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private var foodImageUri: Uri?=null
    private lateinit var foodIngredient: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()


        binding.AddItemButton.setOnClickListener {
            foodName=binding.FoodNameAddMenu.text.toString().trim()
            foodPrice=binding.FoodPriceAddItem.text.toString().trim()
            foodDescription=binding.description.text.toString().trim()
            foodIngredient=binding.ingrediant.text.toString().trim()
            if (!(foodName.isBlank() || foodPrice.isBlank() || foodIngredient.isBlank() || foodDescription.isBlank()))
            {
                uploadData()
                Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else
            {
                Toast.makeText(this, "Fill All The Details", Toast.LENGTH_SHORT).show()
            }
        }
        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun uploadData()
    {
        val menuRef=database.getReference("Menu")
        val newItemKey=menuRef.push().key
        if (foodImageUri != null)
        {
            val storageRef=FirebaseStorage.getInstance().reference
            val imageRef=storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask=imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val newItem=AllMenu(
                        newItemKey,
                        foodName=foodName,
                        foodPrice=foodPrice,
                        foodDescription=foodDescription,
                        foodIngredient=foodIngredient,
                        foodImage=downloadUrl.toString()

                    )
                    newItemKey?.let { key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                            .addOnFailureListener {
                                Toast.makeText(this, "Data Upload Failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }

            }.addOnFailureListener {
                Toast.makeText(this, "ImageUpload Failed", Toast.LENGTH_SHORT).show()
            }

        } else
        {
            Toast.makeText(this, "Please Select An Image", Toast.LENGTH_SHORT).show()
        }

    }

    private val pickImage=registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null)
        {
            binding.selectedImage.setImageURI(uri)
            foodImageUri=uri

        }
    }

}