package com.example.swu_guru


//작성 페이지 DB임. 수강생관리 학교 강의 참고


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager(
        context: Context?,
        name:String?,
        factory:SQLiteDatabase.CursorFactory?,
        version:Int
): SQLiteOpenHelper(context, name, factory, version){
    override fun onCreate(db : SQLiteDatabase?){

        db!!.execSQL("CREATE TABLE personnel(title text, content text, date text)")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}