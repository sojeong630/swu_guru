package com.example.swu_guru

import android.database.Cursor
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

    lateinit var str_title : String
    lateinit var str_price : String
    lateinit var str_place : String
    lateinit var str_content : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_info)

        tvTitle = findViewById(R.id.mTitle)
        tvContent = findViewById(R.id.mContent)
        tvPlace = findViewById(R.id.mPlace)
        tvPrice = findViewById(R.id.mPrice)

        val intent = intent
        str_title=intent.getStringExtra("market_title").toString()

        dbManager=MarketDBManager(this)
        sqlitedb=dbManager.readableDatabase

        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM marketTBL WHERE title='"+str_title+"';", null)

        if(cursor.moveToNext()){
            str_content=cursor.getString((cursor.getColumnIndex("content"))).toString()
            str_price=cursor.getString((cursor.getColumnIndex("price"))).toString()
            str_place=cursor.getString((cursor.getColumnIndex("place"))).toString()

        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()


        //Textview에 값을 넣음
        tvTitle.text=str_title
        tvContent.text=str_content
        tvPrice.text=str_price
        tvPlace.text=str_place


    }
}