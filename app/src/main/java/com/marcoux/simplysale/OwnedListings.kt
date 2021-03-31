package com.marcoux.simplysale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.marcoux.simplysale.databinding.ActivityOwnedListingsBinding

class OwnedListings : AppCompatActivity() {

    private lateinit var binding: ActivityOwnedListingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnedListingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        val model: ListingViewModelOwnerOnly by viewModels()
        model.getListings().observe(this) { listings ->
            val recyclerAdapter = RecyclerViewAdapter(this, listings)
            Log.i("Recycler Binding", "Binding adapter to recycler")
            binding.listingsRecyclerView.adapter = recyclerAdapter
        }

    }
}