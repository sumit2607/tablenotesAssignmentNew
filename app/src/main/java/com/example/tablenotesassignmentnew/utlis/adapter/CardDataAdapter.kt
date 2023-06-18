package com.example.tablenotesassignmentnew.utlis.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tablenotesassignmentnew.R

class CardDataAdapter(private val cardDataList: List<CardData>) :
    RecyclerView.Adapter<CardDataAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView1)

        fun bind(cardData: CardData) {
            titleTextView.text = cardData.name
            descriptionTextView.text = cardData.email
            // Load the image using a library like Picasso or Glide
            if (cardData.image.isNotEmpty()) {
                Glide.with(itemView)
                    .load(cardData.image)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.icons8_facebook_144)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardData = cardDataList[position]
        holder.bind(cardData)
    }

    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount:   ${cardDataList.size}")
        return cardDataList.size
    }
}
