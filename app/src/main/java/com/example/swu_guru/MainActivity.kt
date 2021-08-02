package com.example.swu_guru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    // val db = FirebaseFirestore.getInstance()
    lateinit var marketButton : Button
    lateinit var gbbtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        marketButton = findViewById(R.id.marketButton)
        marketButton.setOnClickListener{
            var intent = Intent(this, SwuMarketActivity::class.java)
            startActivity(intent)

        }
        gbbtn = findViewById(R.id.gbbtn)
        gbbtn.setOnClickListener{
            var intent = Intent(this, GroupBuying::class.java)
            startActivity(intent)

        }

    }
}