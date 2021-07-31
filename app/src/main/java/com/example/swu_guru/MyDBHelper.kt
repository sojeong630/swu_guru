package com.example.swu_guru

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(context: Context) : SQLiteOpenHelper(context, "gbDB", null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys = 1;")

        db!!.execSQL("CREATE TABLE writing (" + "WId INTEGER, " + "title text, " +
                "content text, " + "price INTEGER, " + "person INTEGER, " +
                "count INTEGER, " + "image BLOB, " + "tag INTEGER, " + "PRIMARY KEY(WId AUTOINCREMENT));")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}