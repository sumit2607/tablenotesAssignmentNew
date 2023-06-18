package com.example.tablenotesassignmentnew

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tablenotesassignmentnew.utlis.adapter.CardData
import com.example.tablenotesassignmentnew.utlis.adapter.CardDataAdapter
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: CardDataAdapter
    val notes = mutableListOf<CardData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create an instance of your adapter
        cardAdapter = CardDataAdapter(notes)

        // Attach the adapter to the RecyclerView
        recyclerView.adapter = cardAdapter

        // Fetch data from Firebase
        fetchDataFromFirebase()
    }


    private fun fetchDataFromFirebase() {
        // TODO: Fetch data from Firebase Realtime Database or Firestore
        // Example code for fetching data from Firestore:
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("cards")

        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->

                for (document in querySnapshot) {
                    val title = document.getString("name") ?: ""
                    val content = document.getString("email") ?: ""
                    val note = CardData(title, content)
                    notes.add(note)
                }
                val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                recyclerView.adapter = CardDataAdapter(notes)
            }
            .addOnFailureListener { e ->
                // Handle the failure case
            }

    }

}