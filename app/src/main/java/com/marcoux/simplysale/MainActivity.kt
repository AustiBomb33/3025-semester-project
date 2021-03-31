package com.marcoux.simplysale

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.marcoux.simplysale.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textUserName.setText("Logged in as ${FirebaseAuth.getInstance().currentUser!!.displayName}")
        binding.buttonLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, loginActivity::class.java)
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
    }


}