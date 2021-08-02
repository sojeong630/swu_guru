package com.example.swu_guru

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


//리스트에서 클릭하면 보이는 상세페이지
//작성하고 나서 보이는 info페이지. menu를 통해 리스트로 돌아가거나 삭제, 홈 등 가능


class BorrowInfo : AppCompatActivity(){

    lateinit var dbManager: DBManager
    lateinit var sqlitedb : SQLiteDatabase

    lateinit var tvtitle:TextView
    lateinit var tvcontent:TextView
    lateinit var tvdate : TextView

    lateinit var str_title:String
    lateinit var str_content:String
    lateinit var str_date : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_borrow_info)


        tvtitle=findViewById(R.id.edttitle)
        tvcontent=findViewById(R.id.edtcontent)
        tvdate=findViewById(R.id.edtdate)

        val intent = intent
        str_title=intent.getStringExtra("intent_title").toString()

        //db객체 받아옴
        dbManager=DBManager(this, "personnelDB", null,1)
        sqlitedb=dbManager.readableDatabase

        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM personnel WHERE title='"+str_title+"';", null)

        if(cursor.moveToNext()){
            str_content=cursor.getString((cursor.getColumnIndex("content"))).toString()
            str_date=cursor.getString((cursor.getColumnIndex("date"))).toString()
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()


        //Textview에 값을 넣음
        tvtitle.text=str_title
        tvcontent.text=str_content
        tvdate.text=str_date + "\n"
    }



//option menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_borrow_info,menu)
        return true
    }


//option menu - 홈, 목록, 글쓰기, 삭제

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.action_home ->{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.action_list ->{
                val intent = Intent(this, BorrowList::class.java)
                startActivity(intent)
                return true
            }

            R.id.action_add ->{
                val intent = Intent(this, WriteBorrow::class.java)
                startActivity(intent)
                return true
            }

            R.id.action_remove ->{
                //리스트로 가기 전에 삭제하는 거임

                //db객체 받아옴
                dbManager=DBManager(this, "personnelDB", null,1)
                sqlitedb=dbManager.readableDatabase

                sqlitedb.execSQL("DELETE FROM personnel WHERE title= '"+str_title+"';")
                sqlitedb.close()
                dbManager.close()


                val intent = Intent(this, BorrowList::class.java)
                startActivity(intent)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

}