package com.example.swu_guru

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MarketDBManager (context: Context): SQLiteOpenHelper(context, "swu_market", null, 1){
    override fun onCreate(db: SQLiteDatabase?) {

        db!!.execSQL("CREATE TABLE marketTBL (title text, content text, price INTEGER, place text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}