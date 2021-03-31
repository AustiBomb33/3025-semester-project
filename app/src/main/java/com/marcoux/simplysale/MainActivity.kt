package com.marcoux.simplysale

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
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
    }


}