package com.example.swu_guru

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swu_guru.databinding.ActivitySwuMarketBinding

class SwuMarketActivity : AppCompatActivity() {
    val binding by lazy { ActivitySwuMarketBinding.inflate(layoutInflater) }
    lateinit var dbManager: MarketDBManager
    lateinit var sqlitedb : SQLiteDatabase
    lateinit var mtitle : String
    lateinit var mcost : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dbManager = MarketDBManager(this)

        val data:MutableList<ListLayout> = loadData()
        var adapter = ListAdapter()
        adapter.itemList = data

        binding.marketList.adapter = adapter
        binding.marketList.layoutManager = GridLayoutManager(this, 2)

        binding.writeButton.setOnClickListener{
            var intent = Intent(this, writeActivity::class.java)
            startActivity(intent)
        }


    }
    fun loadData(): MutableList<ListLayout>{
        val data : MutableList<ListLayout> = mutableListOf()
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor

        cursor = sqlitedb.rawQuery("SELECT * FROM marketTBL;", null)
        if(cursor.moveToFirst()){
            do{
                mtitle = cursor.getString(cursor.getColumnIndex("title")).toString()
                mcost = cursor.getString(cursor.getColumnIndex("price")).toString()

                var title = mtitle
                var cost = mcost
                var listlayout = ListLayout(title, cost )
                data.add(listlayout)
            }while (cursor.moveToNext())
        }
        cursor.close()
        sqlitedb.close()

        return data;
    }

}