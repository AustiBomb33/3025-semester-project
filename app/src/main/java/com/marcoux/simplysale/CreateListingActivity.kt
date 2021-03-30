package com.marcoux.simplysale

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.marcoux.simplysale.databinding.ActivityCreateListingBinding
import com.marcoux.simplysale.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream

class CreateListingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateListingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateListingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backButton = findViewById<Button>(R.id.backButton);
        backButton.setOnClickListener {
            finish()
        }

        val submitButton = findViewById<Button>(R.id.buttonSubmitListing);
        submitButton.setOnClickListener {
            println("Submitting listing")
            if (binding.textItemPrice.text.isNotEmpty()
                && binding.textDescription.text.isNotEmpty()
                && binding.textItemName.text.isNotEmpty()
            ) {
                //all fields are not empty
                val newListing = Listing()
                val db = FirebaseFirestore.getInstance().collection("listings")
                val storage = FirebaseStorage.getInstance().reference
                //set easy text values
                newListing.name = binding.textItemName.text.toString()
                newListing.desc = binding.textDescription.text.toString()
                newListing.price = binding.textItemPrice.text.toString().toFloat()

                //get an ID for the listing
                newListing.id = db.document().id

                //create image URL
                val imageURL = newListing.id + newListing.name
                var imageRef = storage.child("images/$imageURL.jpg")
                newListing.image = imageURL

                //upload image to firebase storage as JPG
                val bitmap = (binding.buttonSelectImage.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val upload = imageRef.putBytes(data)

                upload.addOnSuccessListener {
                    //push to database if succeeded
                    val dbPush = db.document(newListing.id!!).set(newListing)
                    dbPush.addOnSuccessListener {
                        //reset fields, toast success
                        Toast.makeText(this, "Listing posted!", Toast.LENGTH_LONG).show()
                        binding.textDescription.setText("")
                        binding.textItemName.setText("")
                        binding.textItemPrice.setText("")
                    }.addOnFailureListener{
                        //toast failure
                        Toast.makeText(this, "Push to database failed", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{
                    //alert user if failed
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_LONG).show()
                    println("failed to submit: image not uploaded")
                }



            } else {
                //a field is empty
                Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show()
                println("failed to submit: fields empty")
            }
        }

    }
}