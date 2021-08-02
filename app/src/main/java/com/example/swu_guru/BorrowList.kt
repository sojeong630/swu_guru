package com.example.swu_guru

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

//물건 빌려주는 첫 리스트 페이지


class BorrowList : AppCompatActivity(){

    lateinit var sqlitedb : SQLiteDatabase

    lateinit var borrowtitle : String
    lateinit var borrowname : String


    //part9부분 코드
    lateinit var dbManager: DBManager
    lateinit var layout: LinearLayout
    //part9부분 코드드


    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_borrow_list)


        //part9부분 코드
        dbManager = DBManager(this,"personnelDB",null,1)
        sqlitedb=dbManager.readableDatabase
        layout=findViewById(R.id.personnel)
        layout=findViewById(R.id.personnel)

        var cursor : Cursor
        cursor=sqlitedb.rawQuery("SELECT * FROM personnel", null)

        var num : Int=0
        while(cursor.moveToNext()){

            //현재 커서의 값들을 변수로 받아옴
            var str_title=cursor.getString(cursor.getColumnIndex("title")).toString()
            var str_content=cursor.getString(cursor.getColumnIndex("content")).toString()
            var str_date=cursor.getString(cursor.getColumnIndex("date")).toString()

            //텍스트뷰가 들어있는 레이아웃
            var layout_item:LinearLayout = LinearLayout(this)
            layout_item.orientation=LinearLayout.VERTICAL
            layout_item.id=num


            //구분을 위해서 title 부분만 폰트 사이즈와 컬러
            var tvtitle : TextView= TextView(this)
            tvtitle.text=str_title
            tvtitle.textSize=30f
            tvtitle.setBackgroundColor(Color.LTGRAY)
            layout_item.addView(tvtitle)

            var tvcontent : TextView=TextView(this)
            tvcontent.text=str_content
            tvtitle.textSize=15f
            layout_item.addView(tvcontent)

            var tvdate : TextView=TextView(this)
            tvdate.text=str_date
            tvtitle.textSize=15f
            layout_item.addView(tvdate)


            //클릭하면 상세페이지로 넘어감
            layout_item.setOnClickListener{
                val intent=Intent(this, BorrowInfo :: class.java)
                intent.putExtra("intent_title",str_title)
                startActivity(intent)
            }

            layout.addView(layout_item)
            num++;
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()

    }




//option menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_borrow_list,menu)
        return true
    }


//option menu - 홈, 등록

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {

            R.id.action_home ->{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }


            R.id.action_add ->{
                val intent = Intent(this, WriteBorrow::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }




}