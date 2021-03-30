package com.marcoux.simplysale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.marcoux.simplysale.databinding.ActivityListingsBinding
import com.marcoux.simplysale.databinding.ActivityMainBinding

class ListingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListingsBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i("Test", "List view\n\n\n\n")
        val model: ListingViewModel by viewModels()
        model.getListings().observe(this) { listings ->
            var recyclerAdapter = RecyclerViewAdapter(this, listings)
            Log.i("Recycler Binding", "Binding adapter to recycler")
            binding.listingsRecyclerView.adapter = recyclerAdapter
        }
    }
}