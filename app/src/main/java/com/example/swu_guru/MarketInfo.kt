package com.example.swu_guru

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class MarketInfo : AppCompatActivity() {
    lateinit var dbManager: MarketDBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var tvTitle:TextView
    lateinit var tvPrice:TextView
    lateinit var tvPlace:TextView
    lateinit var tvContent:TextView
    lateinit var mContact :Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_info)

        tvTitle = findViewById(R.id.mTitle)
        tvContent = findViewById(R.id.mContent)
        tvPlace = findViewById(R.id.mPlace)
        tvPrice = findViewById(R.id.mPrice)
        mContact = findViewById(R.id.mContact)

    }
}