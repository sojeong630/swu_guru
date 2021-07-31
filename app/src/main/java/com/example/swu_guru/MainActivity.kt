package com.example.swu_guru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var btnTest: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTest = findViewById(R.id.btntest)

        btnTest.setOnClickListener {
            val intent = Intent(this, GroupBuying::class.java)
            startActivity(intent)
        }
    }
}