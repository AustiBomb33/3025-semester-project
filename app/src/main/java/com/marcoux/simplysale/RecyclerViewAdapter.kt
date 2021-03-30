package com.marcoux.simplysale

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import java.net.URLEncoder

class RecyclerViewAdapter(val context: Context, val listings: List<Listing>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ListingViewHolder>() {

    inner class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.textName)
        val priceTextView = itemView.findViewById<TextView>(R.id.textPrice)
        val itemImageView = itemView.findViewById<ImageView>(R.id.image)
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
        with(holder){
            nameTextView.text = listing.name
            priceTextView.text = "$${listing.price}"

            //get image from storage
            val storage = FirebaseStorage.getInstance().reference
            //val imgURL = URLEncoder.encode("images/${listing.image}.jpg", "utf-8")
            val imgURL = "images/${listing.image}.jpg"
            Log.i("ImageURL", imgURL)
            val storagePath = storage.child(imgURL)
            storagePath.getBytes(1024*1024).addOnSuccessListener {bytes->
                itemImageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            }

        }
    }


}