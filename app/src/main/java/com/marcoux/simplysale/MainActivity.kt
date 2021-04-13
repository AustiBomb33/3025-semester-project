package com.marcoux.simplysale

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.marcoux.simplysale.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textUserName.setText("Logged in as ${FirebaseAuth.getInstance().currentUser!!.displayName}")
        binding.buttonLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val buttonListingsActivity = findViewById<Button>(R.id.buttonViewListings)
        buttonListingsActivity.setOnClickListener {
            val intent = Intent(this, ListingsActivity::class.java)
            startActivity(intent)
        }

        val buttonCreateListingActivity = findViewById<Button>(R.id.buttonCreateListings)
        buttonCreateListingActivity.setOnClickListener {
            val intent = Intent(this, CreateListingActivity::class.java)
            startActivity(intent)
        }

        val buttonModifyListing = findViewById<Button>(R.id.buttonModifyListings)
        buttonModifyListing.setOnClickListener {
            val intent = Intent(this, OwnedListings::class.java)
            startActivity(intent)
        }

        val model: ListingViewModel by viewModels()
        model.getListings().observe(this) { listings ->
            val size = listings.size

            Log.i("model size", "" + size)
            val featured = model.getListings().value!![Random.nextInt(0, size)]
            binding.featuredName.text = featured.name

            val storage = FirebaseStorage.getInstance().reference
            val imgURL = "images/${featured.image}.jpg"
            Log.i("ImageURL", imgURL)
            val storagePath = storage.child(imgURL)
            storagePath.getBytes(1024 * 1024 * 1024).addOnSuccessListener { bytes ->
                binding.featuredImage.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        bytes,
                        0,
                        bytes.size
                    )
                )
            }

            binding.featuredListing.setOnClickListener {
                Log.i("Clicked listing", "${featured.name}")
                val intent: Intent =
                    if (featured.owner == FirebaseAuth.getInstance().currentUser!!.uid) {
                        //modify the listing if user owns it
                        Intent(this, ModifyListingActivity::class.java)
                    } else {
                        //otherwise, just view it
                        Intent(this, ViewListingActivity::class.java)
                    }
                intent.putExtra("listing", featured)
                startActivity(intent)
            }

        }
    }


}