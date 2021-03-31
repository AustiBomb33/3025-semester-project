package com.marcoux.simplysale

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.marcoux.simplysale.databinding.ActivityListingsBinding

class ListingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        val model: ListingViewModel by viewModels()
        model.getListings().observe(this) { listings ->
            val recyclerAdapter = RecyclerViewAdapter(this, listings)
            Log.i("Recycler Binding", "Binding adapter to recycler")
            binding.listingsRecyclerView.adapter = recyclerAdapter
        }
    }
}