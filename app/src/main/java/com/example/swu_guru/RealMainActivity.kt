package com.example.swu_guru

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.swu_guru.R
import com.example.swu_guru.RegisterActivity
import kotlinx.android.synthetic.main.activity_market_info.*
import kotlinx.android.synthetic.main.activity_real_main.*


class RealMainActivity : AppCompatActivity() {
    val TAG: String = "RealMainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_real_main)


        // 로그인 버튼
        btn_login.setOnClickListener {

            //editText로부터 입력된 값을 받아온다
            var id = edit_id.text.toString()
            var pw = edit_pw.text.toString()

            // 쉐어드로부터 저장된 id, pw 가져오기
            val sharedPreference = getSharedPreferences("file name", Context.MODE_PRIVATE)
            val savedId = sharedPreference.getString("id", "")
            val savedPw = sharedPreference.getString("pw", "")

            // 유저가 입력한 id, pw값과 쉐어드로 불러온 id, pw값 비교
            if(id == savedId && pw == savedPw){
                // 로그인 성공 다이얼로그 보여주기
                dialog("success")

                // 올릴때 여기 삭제
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                // 로그인 실패 다이얼로그 보여주기
                dialog("fail")
            }

            //로그인 후 다른 액티비티로 전환하기
            val nextIntent = Intent(this, MainActivity::class.java)
            //전환된 액티비티에 데이터 값 전달
            nextIntent.putExtra("id", savedId) //key: "email", value: inputEmail
            startActivity(nextIntent)
        }

        // 회원가입 버튼
        btn_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    // 로그인 성공/실패 시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)

        if(type.equals("success")){
            dialog.setTitle("로그인 성공")
            dialog.setMessage("로그인 성공!")

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        else if(type.equals("fail")){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }

        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()

    }
}