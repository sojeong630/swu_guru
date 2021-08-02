package com.example.swu_guru

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

// 마이페이지
class Mypage : AppCompatActivity() {
    lateinit var myDBHelper: MyDBHelper
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var idtext: TextView
    lateinit var id: String
    lateinit var nickname: TextView
    lateinit var nicknameBtn: Button
    lateinit var submit: Button
    lateinit var imagechangeBtn: Button
    lateinit var profileImage: CircleImageView
    lateinit var nicknameResult: String
    val REQUEST_READ_EXTERNAL_STORAGE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)
        id = intent.getStringExtra("id").toString()
        myDBHelper = MyDBHelper(this)
        sqlitedb = myDBHelper.readableDatabase
        sqlitedb = myDBHelper.writableDatabase

        nickname = findViewById(R.id.nickname)
        nicknameBtn = findViewById(R.id.nicknameBtn)
        imagechangeBtn = findViewById(R.id.imagechangeBtn)
        profileImage = findViewById(R.id.profileImage)
        submit = findViewById(R.id.submit)




        // 로그인/회원가입 하며 putExtra()한 아이디 및 DB 저장된 정보 불러오기
        idtext = findViewById(R.id.idtext)
        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM user WHERE id = '${id}';", null)


        if (cursor.moveToFirst()) {
            nicknameResult = nickname.text.toString()

            id = cursor.getString(cursor.getColumnIndex("id")).toString()
            nicknameResult = cursor.getString(cursor.getColumnIndex("nickname")).toString()

            // BLOB 상태의 이미지 처리
           /* try {
                val v_image = cursor.getBlob(cursor.getColumnIndex("image")) ?: null
                val bitmap = BitmapFactory.decodeByteArray(v_image, 0, v_image!!.size)
                profileImage.setImageBitmap(bitmap)
            } catch (knpe: KotlinNullPointerException) {
            }*/

            idtext.text = id
            nickname.text = nicknameResult

        }


        // 닉네임 변경 버튼 클릭시
        nicknameBtn.setOnClickListener {
            val dialog = CustomDialog(this)
            dialog.myDig()
            dialog.setOnClickedListener(object:CustomDialog.ButtonClickListener{
                override fun onClicked(myName: String) {
                    nickname.setText(myName.toString())
                    Log.d("닉네임 변경", "$nickname")
                }
            })
        }

        // 이미지 변경 버튼 클릭 시
        imagechangeBtn.setOnClickListener {
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

        // 저장 버튼 클릭 시
        submit.setOnClickListener {
            sqlitedb = myDBHelper.writableDatabase
            sqlitedb = myDBHelper.readableDatabase


            var image: Drawable = profileImage.drawable
            var byteArray: ByteArray ?= null
            nicknameResult = nickname.text.toString()
            Log.d("닉네임 변경", "$nicknameResult")
            sqlitedb.execSQL("UPDATE user SET " + "nickname = '$nicknameResult' WHERE id = '$id'")

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

            // 글 내용과 이미지 DB에 저장
            if(byteArray != null) {
                // var insQuery: String = "INSERT INTO writing (WId, title, content, price, person, count, image, tag) " +
                        /*"VALUES (null, '$title', '$content', $price, $person, $count, ?, $tag)"*/

                var insQuery: String = "UPDATE user SET " + "image = ? WHERE id = '$id'"
                var stmt: SQLiteStatement = sqlitedb.compileStatement(insQuery)
                stmt.bindBlob(1, byteArray)
                stmt.execute()
            }


        }
    } // onCreate 끝

    // 닉네임 변경시 사용 (커스텀 다이얼로그)
    class CustomDialog(context: Context){
        private val dialog = Dialog(context)

        fun myDig(){
            dialog.setContentView(R.layout.mypagedialog)
            dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setCancelable(true)
            dialog.show()

            val changeNickname = dialog.findViewById<EditText>(R.id.changeNickname)
            val changeNicknameOK = dialog.findViewById<Button>(R.id.changeNicknameOK)

            changeNicknameOK.setOnClickListener {
                onClickedListener.onClicked(changeNickname.text.toString())
                dialog.dismiss()
            }
        }

        interface ButtonClickListener {
            fun onClicked(myName: String)
        }

        private lateinit var onClickedListener: ButtonClickListener
        fun setOnClickedListener(listener: ButtonClickListener) {
            onClickedListener = listener
        }
    }

    // 갤러리에서 이미지 선택 시 작동
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data != null){
            if(requestCode == 1){
                try {
                    var imageUri : Uri? = data?.data
                    profileImage.setImageURI(imageUri)
                } catch (e:Exception) {}

            }
        }
    }

}