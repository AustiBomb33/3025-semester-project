package com.marcoux.simplysale

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.marcoux.simplysale.databinding.ActivityModifyListingBinding
import java.io.ByteArrayOutputStream

class ModifyListingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModifyListingBinding

    private fun pickImageIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select an image"), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            //if request was okay, we assume the image was chosen
            binding.buttonSelectImage.setImageURI(data!!.data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyListingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listing = intent.getSerializableExtra("listing") as? Listing
        binding.textDescription.setText(listing!!.desc)
        binding.textItemName.setText(listing.name)
        binding.textItemPrice.setText("${listing.price}")

        val storage = FirebaseStorage.getInstance().reference
        val imgURL = "images/${listing.image}.jpg"
        Log.i("ImageURL", imgURL)
        val storagePath = storage.child(imgURL)
        storagePath.getBytes(1024 * 1024 * 1024).addOnSuccessListener { bytes ->
            binding.buttonSelectImage.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    bytes,
                    0,
                    bytes.size
                )
            )
        }

        binding.buttonSelectImage.setOnClickListener {
            pickImageIntent()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.buttonSubmitListing.setOnClickListener {
            //remove old image
            storage.child("images/${listing.image}.jpg").delete()

            listing.desc = binding.textDescription.text.toString()
            listing.name = binding.textItemName.text.toString()
            listing.price = binding.textItemPrice.text.toString().toFloat()

            //create image URL
            val imageURL = listing.id + listing.name
            val imageRef = storage.child("images/$imageURL.jpg")
            Log.i("ImageURL", "images/$imageURL.jpg")
            listing.image = imageURL

            //upload image to firebase storage as JPG
            val bitmap = (binding.buttonSelectImage.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val upload = imageRef.putBytes(data)

            val db = FirebaseFirestore.getInstance().collection("listings")
            upload.addOnSuccessListener {
                //push to database if succeeded
                val dbPush = db.document(listing.id!!).set(listing)
                dbPush.addOnSuccessListener {
                    //reset fields, toast success
                    Toast.makeText(this, "Listing updated!", Toast.LENGTH_LONG).show()
                    finish()
                }.addOnFailureListener {
                    //toast failure
                    Toast.makeText(this, "Push to database failed", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                //alert user if failed
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_LONG).show()
                Log.e("failed to submit", "image not uploaded")
            }
        }

    }
}