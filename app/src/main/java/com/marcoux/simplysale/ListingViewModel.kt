package com.marcoux.simplysale

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ListingViewModel : ViewModel() {
    private val listings = MutableLiveData<List<Listing>>()

    init {
        loadListings()
    }

    fun getListings() : LiveData<List<Listing>>{
        return listings
    }

    private fun loadListings() {
        val db = FirebaseFirestore.getInstance().collection("listings")
            .orderBy("name", Query.Direction.ASCENDING)

        db.addSnapshotListener { documents, exception ->
            Log.i("DB_RESPONSE", "Returned ${documents?.size()} items")
            if (exception != null) {
                Log.w("DB_RESPONSE", exception)
                return@addSnapshotListener
            }

            documents?.let {
                val listingsTemp = ArrayList<Listing>()
                for (document in documents) {
                    val listing = document.toObject(Listing::class.java)
                    listingsTemp.add(listing)
                }
                listings.value = listingsTemp
            }
        }
    }
}