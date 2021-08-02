package com.example.swu_guru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var btnTest4 : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    btnTest4 = findViewById(R.id.btntest4)
    btnTest4.setOnClickListener {
        val intent = Intent(this, BorrowList::class.java)
        startActivity(intent)
    }
}