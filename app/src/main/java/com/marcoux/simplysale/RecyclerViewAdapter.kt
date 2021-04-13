package com.marcoux.simplysale

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class RecyclerViewAdapter(private val context: Context, private val listings: List<Listing>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ListingViewHolder>() {

    inner class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.textName)!!
        val priceTextView = itemView.findViewById<TextView>(R.id.textPrice)!!
        val itemImageView = itemView.findViewById<ImageView>(R.id.image)!!
        val listingRoot = itemView.findViewById<ConstraintLayout>(R.id.listingRoot)!!
    }

    override fun getItemCount(): Int {
        return listings.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_listing, parent, false)
        return ListingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        val listing = listings[position]
        with(holder) {
            nameTextView.text = listing.name
            priceTextView.text = "$${listing.price}"

            //get image from storage
            val storage = FirebaseStorage.getInstance().reference
            val imgURL = "images/${listing.image}.jpg"
            Log.i("ImageURL", imgURL)
            val storagePath = storage.child(imgURL)
            storagePath.getBytes(1024 * 1024 * 1024).addOnSuccessListener { bytes ->
                itemImageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            }
            listingRoot.setOnClickListener {
                Log.i("Clicked listing", "${listing.name}")
                val intent: Intent =
                    if (listing.owner == FirebaseAuth.getInstance().currentUser!!.uid) {
                        //modify the listing if user owns it
                        Intent(context, ModifyListingActivity::class.java)
                    } else {
                        //otherwise, just view it
                        Intent(context, ViewListingActivity::class.java)
                    }
                intent.putExtra("listing", listing)
                startActivity(context, intent, null)
            }
        }
    }


}