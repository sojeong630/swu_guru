package com.example.swu_guru

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

//작성페이지 코틀린 파일 학교 강의로는 PersonnelReg


class WriteBorrow : AppCompatActivity() {


    //변수

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var btnRegist: Button
    lateinit var edttitle: EditText
    lateinit var edtcontent: EditText
    lateinit var edtdate: EditText


    //변수와 위젯 연결

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_borrow)

        btnRegist = findViewById(R.id.btnRegister)
        edttitle = findViewById(R.id.edttitle)
        edtcontent = findViewById(R.id.edtcontent)
        edtdate = findViewById(R.id.edtdate)


        //dbmanager가 객체 받아옴
        dbManager = DBManager(this, "personnelDB", null, 1)

        //버튼이 눌리면 database에 저장, 이후에 intent로 다음 화면(BorrowList) 넘어감

        btnRegist.setOnClickListener {
            var str_title: String = edttitle.text.toString()
            var str_content: String = edtcontent.text.toString()
            var str_date: String = edtdate.text.toString()

            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("INSERT INTO personnel VALUES ('" + str_title + "', '" + str_content + "','" + str_date + "')")
            sqlitedb.close()


            val intent=Intent(this, BorrowInfo::class.java)
            //어떤 것이 새로 추가된 것인지 정보값을 넘김
            intent.putExtra("intent_title", str_title)
            startActivity(intent)


        }

    }


    //option menu

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_write_borrow,menu)
        return true
    }


    //option menu - 홈, 목록

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.action_home ->{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.action_back ->{
                val intent = Intent(this, BorrowList::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}


