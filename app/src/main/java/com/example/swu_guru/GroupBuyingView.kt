package com.example.swu_guru

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import android.view.View
import android.widget.*
import org.w3c.dom.Text

// 공동구매 상품 상세페이지
class GroupBuyingView : AppCompatActivity() {
    lateinit var myDBHelper: MyDBHelper
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var vimageview: ImageView
    lateinit var vtitle: TextView
    lateinit var vprice: TextView
    lateinit var vcontent: TextView
    lateinit var vcount: TextView
    lateinit var vperson: TextView
    lateinit var vparticipate: Button
    lateinit var textparticipate: TextView


    lateinit var v_title: String
    lateinit var v_content: String
    lateinit var v_price: String
    var v_id: Int = 0
    var v_person: Int = 0
    var v_count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_buying_view)

        vimageview = findViewById(R.id.vimageview)
        vtitle = findViewById(R.id.vtitle)
        vprice = findViewById(R.id.vprice)
        vcontent = findViewById(R.id.vcontent)
        vcount = findViewById(R.id.vcount)
        vperson = findViewById(R.id.vperson)
        vparticipate = findViewById(R.id.vparticipate)
        textparticipate = findViewById(R.id.textparticipate)

        // CustomAdapter.kt에서 받아온 id값
        val intent = intent
        v_id = Integer.parseInt(intent.getStringExtra("intent_id"))

        myDBHelper = MyDBHelper(this)
        sqlitedb = myDBHelper.readableDatabase
        sqlitedb = myDBHelper.writableDatabase

        // 받아온 id값(고유값)으로 db에서 해당하는 필드 불러오기
        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM writing WHERE WId = '${v_id}';", null)

        if (cursor.moveToFirst()) {
            v_title = cursor.getString(cursor.getColumnIndex("title")).toString()
            v_content = cursor.getString(cursor.getColumnIndex("content")).toString()
            v_price = cursor.getString(cursor.getColumnIndex("price")).toString()
            v_person = cursor.getInt(cursor.getColumnIndex("person"))
            v_count = cursor.getInt(cursor.getColumnIndex("count"))

            // BLOB 상태의 이미지 처리
            try {
                val v_image = cursor.getBlob(cursor.getColumnIndex("image")) ?: null
                val bitmap = BitmapFactory.decodeByteArray(v_image, 0, v_image!!.size)
                vimageview.setImageBitmap(bitmap)
            } catch (knpe: KotlinNullPointerException) {
            }

            vtitle.text = v_title
            vcontent.text = v_content
            vprice.text = v_price + "원"
            vcount.text = "" + v_count
            vperson.text = "" + v_person

            // 인원이 다 차면 참여버튼 비활성화 및 안내텍스트 출력
            if(v_count == v_person) {
                vparticipate.setEnabled(false)
                textparticipate.text = "더 이상 참여할 수 없는 거래입니다."
            }
        }

        // 참여하기 버튼 클릭 -> count(참여중인 인원) 증가
        // 참여인원이 다 차면 메세지 띄우기 (참여할 수 없는 거래입니다)
        // 추후 로그인 기능 구현되면 연동하기
        vparticipate.setOnClickListener {
            v_count += 1
            sqlitedb.execSQL("UPDATE writing SET " + "count = $v_count WHERE WId = $v_id")
            var cursor: Cursor = sqlitedb.rawQuery("SELECT * FROM writing WHERE WId = '${v_id}';", null)

                if (cursor.moveToFirst()) {
                    v_count = cursor.getInt(cursor.getColumnIndex("count"))
                    vcount.text = "" + v_count
                }
            Toast.makeText(this, "참여가 완료되었습니다", Toast.LENGTH_SHORT).show()
            vparticipate.setEnabled(false)

            if(v_count == v_person) {
                textparticipate.text = "더 이상 참여할 수 없는 거래입니다."
                sqlitedb.execSQL("UPDATE writing SET " + "tag = 1 WHERE WId = $v_id")
            }
        }
    }
}

