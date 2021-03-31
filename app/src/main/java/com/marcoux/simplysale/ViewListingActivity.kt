package com.marcoux.simplysale

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.text.set
import com.google.firebase.storage.FirebaseStorage
import com.marcoux.simplysale.databinding.ActivityViewListingBinding

class ViewListingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewListingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewListingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listing = intent.getSerializableExtra("listing") as? Listing
        binding.textDescription.setText(listing!!.desc)
        binding.textItemName.setText(listing.name)
        binding.textItemPrice.setText("$${listing.price}")

        val storage = FirebaseStorage.getInstance().reference
        val imgURL = "images/${listing.image}.jpg"
        Log.i("ImageURL", imgURL)
        val storagePath = storage.child(imgURL)
        storagePath.getBytes(1024 * 1024).addOnSuccessListener { bytes ->
            binding.buttonSelectImage.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    bytes,
                    0,
                    bytes.size
                )
            )
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}