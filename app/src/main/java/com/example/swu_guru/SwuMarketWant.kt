package com.example.swu_guru

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swu_guru.databinding.ActivitySwuMarketWantBinding

class SwuMarketWant : AppCompatActivity() {
    val binding by lazy { ActivitySwuMarketWantBinding.inflate(layoutInflater) }
    lateinit var dbManager: MarketDBManager
    lateinit var sqlitedb : SQLiteDatabase
    lateinit var mtitle : String
    lateinit var mcost : String
    lateinit var buyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dbManager = MarketDBManager(this)

        buyButton = findViewById(R.id.buyButton)

        val data:MutableList<ListLayout> = loadData()
        var adapter = MarketWantListAdapter()
        adapter.itemList = data

        binding.marketList.adapter = adapter
        binding.marketList.layoutManager = LinearLayoutManager(this)


        binding.writeButton.setOnClickListener{
            var intent = Intent(this, writeActivity::class.java)
            intent.putExtra("key", "want")
            startActivity(intent)
        }

        buyButton.setOnClickListener{
            var intent = Intent(this, SwuMarketActivity::class.java)
            startActivity(intent)
        }

    }
    fun loadData(): MutableList<ListLayout>{
        val data : MutableList<ListLayout> = mutableListOf()
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor

        cursor = sqlitedb.rawQuery("SELECT * FROM marketWantTBL;", null)
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