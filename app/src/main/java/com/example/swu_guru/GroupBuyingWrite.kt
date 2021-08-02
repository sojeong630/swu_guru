package com.example.swu_guru

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.renderscript.ScriptIntrinsicYuvToRGB
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.sql.Types.NULL

// 공동구매 글쓰기 페이지
class GroupBuyingWrite : AppCompatActivity() {


    lateinit var myDBHelper: MyDBHelper
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var edtGBWtitle: EditText
    lateinit var edtGBWprice: EditText
    lateinit var edtGBWperson: EditText
    lateinit var edtGBWcontent: EditText
    lateinit var imgGBWimageview: ImageView
    lateinit var btnGBWimage: Button
    lateinit var btnGBWregister: Button
    var GBWcount: Int=1
    var id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_buying_write)

        edtGBWtitle = findViewById(R.id.GBWTitle)
        edtGBWprice = findViewById(R.id.GBWPrice)
        edtGBWperson = findViewById(R.id.GBWPerson)
        edtGBWcontent = findViewById(R.id.GBWContents)
        imgGBWimageview = findViewById(R.id.GBWImageview)
        btnGBWimage = findViewById(R.id.GBWImage)
        btnGBWregister = findViewById(R.id.GBWRegist)
        val REQUEST_READ_EXTERNAL_STORAGE = 1000


        myDBHelper = MyDBHelper(this)
        id = intent.getIntExtra("ID", 0)

        // 이미지 선택 버튼 클릭 시
        btnGBWimage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용되지 않음
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
            }
            else {
                // 권한이 이미 허용됨
                var intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, 1)
            }
        }

        // 게시물 등록 버튼 클릭 시
        btnGBWregister.setOnClickListener {

            sqlitedb = myDBHelper.writableDatabase
            sqlitedb = myDBHelper.readableDatabase

            var title: String = edtGBWtitle.text.toString()
            var content: String = edtGBWcontent.text.toString()
            var price: String = edtGBWprice.text.toString()
            var person: String = edtGBWperson.text.toString()
            var count: String = GBWcount.toString()
            var image: Drawable = imgGBWimageview.drawable
            var tag: String = "0" // 진행중이면 0, 거래완료시 1
            var byteArray: ByteArray ?= null

            // 이미지 파일 -> bitmap -> byteArray -> BLOB
            try {
                val bitmapDrawable = image as BitmapDrawable?
                var bitmap: Bitmap? = bitmapDrawable?.bitmap
                if(bitmap != null) { // 이미지 파일이 존재하면 리사이즈 (500*500)
                    bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true)
                }
                val stream = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.PNG, 70, stream)
                byteArray = stream.toByteArray()
                Log.d("image save", "이미지 저장")
            } catch (cce: ClassCastException) {Log.d("image null", "이미지 저장 안함")}

            // 글 내용이 있어야만 등록 가능하도록 추후 설정

            // 글 내용과 이미지 DB에 저장
            if(byteArray == null) { // 이미지 미선택시 -> 기본 이미지 출력
                sqlitedb.execSQL("INSERT INTO writing VALUES (null, '$title', '$content', $price, $person, $count, null, $tag)")
            } else { // 이미지 선택 시
                var insQuery: String = "INSERT INTO writing (WId, title, content, price, person, count, image, tag) " +
                        "VALUES (null, '$title', '$content', $price, $person, $count, ?, $tag)"
                var stmt: SQLiteStatement = sqlitedb.compileStatement(insQuery)
                stmt.bindBlob(1, byteArray)
                stmt.execute()
            }

            Log.d("intent id", "$id")
            // 화면 이동 -> 공동구매 상품 상세 페이지
            val intent = Intent(this, GroupBuying::class.java)
            startActivity(intent)
        }


    } // onCreate 끝

    // 갤러리에서 이미지 선택 시 작동
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data != null){
            if(requestCode == 1){
                try {
                    var imageUri : Uri? = data?.data
                    imgGBWimageview.setImageURI(imageUri)
                } catch (e:Exception) {}

            }
        }
    }

}