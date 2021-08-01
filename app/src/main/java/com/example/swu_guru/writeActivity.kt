package com.example.swu_guru

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

class writeActivity : AppCompatActivity() {
    lateinit var dbManager: MarketDBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var mbtnUp: Button
    lateinit var mContent : EditText
    lateinit var mPrice : EditText
    lateinit var mTitle : EditText
    lateinit var mPlace : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        mbtnUp = findViewById(R.id.mbtnUp)
        mContent = findViewById(R.id.mContent)
        mPrice = findViewById(R.id.mPrice)
        mTitle = findViewById(R.id.mTitle)
        mPlace = findViewById(R.id.mPlace)
        val REQUEST_READ_EXTERNAL_STORAGE = 1000

        dbManager = MarketDBManager(this)

        //쓰기 버튼 클릭 시
        mbtnUp.setOnClickListener{
            var title : String = mTitle.text.toString()
            var price : String = mPrice.text.toString()
            var content: String = mContent.text.toString()
            var place : String = mPlace.text.toString()

            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("INSERT INTO marketTBL VALUES ('"+ title +"', '"+ content +"', "+ price +", '"+ place +"');")



            val intent = Intent(this, SwuMarketActivity::class.java)
            startActivity(intent)

        }
    }

}