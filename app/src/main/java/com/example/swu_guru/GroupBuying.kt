package com.example.swu_guru

import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.swu_guru.databinding.ActivityGroupBuyingBinding


// 공동구매 리사이클러뷰 (목록) 페이지
class GroupBuying : AppCompatActivity() {
    lateinit var myDBHelper: MyDBHelper
    lateinit var sqlitedb: SQLiteDatabase
    val binding by lazy { ActivityGroupBuyingBinding.inflate(layoutInflater) }
    lateinit var gtitle: String
    lateinit var gprice: String
    lateinit var btnwrite: Button
    var gid: Int = 0
    var gperson: Int = 0
    var gcount: Int = 0
    lateinit var switchprogress: Switch
    lateinit var textprogress: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        btnwrite = (findViewById(R.id.btnwrite))
        switchprogress = (findViewById(R.id.switchprogress))
        textprogress = (findViewById(R.id.textprogress))

        myDBHelper = MyDBHelper(this)

        val data:MutableList<Memo> = loadData()
        var adapter = CustomAdapter()
        adapter.listData = data
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        btnwrite.setOnClickListener {
            val intent = Intent(this, GroupBuyingWrite::class.java)
            startActivity(intent)
        }

        // 스위치 ON -> 진행중인 거래만 보기, OFF -> 모든 거래 보기
        switchprogress.setOnCheckedChangeListener {
            buttonView, isChecked ->
            if (isChecked) {
                textprogress.setTextColor(Color.parseColor("#8B00FF"))
                textprogress.setTypeface(null, Typeface.BOLD)
                val data:MutableList<Memo> = loadData2()
                var adapter = CustomAdapter()
                adapter.listData = data
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
                loadData2()
            } else {
                textprogress.setTextColor(Color.parseColor("#000000"))
                textprogress.setTypeface(null, Typeface.NORMAL)
                val data:MutableList<Memo> = loadData()
                var adapter = CustomAdapter()
                adapter.listData = data
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
                loadData()
            }
        }
        
    }
    fun loadData(): MutableList<Memo>{
        val data: MutableList<Memo> = mutableListOf()
        sqlitedb = myDBHelper.readableDatabase
        var cursor: Cursor

        cursor = sqlitedb.rawQuery("SELECT * FROM writing;", null)
        if (cursor.moveToFirst()) {
            do {
                gid = cursor.getInt(cursor.getColumnIndex("WId"))
                Log.d("id tag", "${gid}")
                gtitle = cursor.getString(cursor.getColumnIndex("title")).toString()
                gprice = cursor.getString(cursor.getColumnIndex("price")).toString()
                gperson = cursor.getInt(cursor.getColumnIndex("person"))
                gcount = cursor.getInt(cursor.getColumnIndex("count"))

                val vimage = cursor.getBlob(cursor.getColumnIndex("image")) ?: null
                val bitmap = BitmapFactory.decodeByteArray(vimage, 0, vimage!!.size)
                var image = bitmap
                if(gperson == gcount) { // 인원이 다 찼으면 워터마크 출력, tag 1로 변경
                    image = addWatermark(baseContext.getResources(), image)
                    sqlitedb.execSQL("UPDATE writing SET " + "tag = 1 WHERE WId = $gid")
                }
                var id = gid
                var title = gtitle
                var price = gprice + "원"
                var person = gperson
                var count = gcount
                var memo = Memo(id, title, price, count, person, image)
                data.add(memo)
            } while (cursor.moveToNext())
        }
        cursor.close()
        sqlitedb.close()

        return data;
    }

    // 진행중인 거래만 보기 스위치 ON시 사용 -> 태그 0만 불러와서 출력
    fun loadData2(): MutableList<Memo>{
        val data: MutableList<Memo> = mutableListOf()
        sqlitedb = myDBHelper.readableDatabase
        var cursor: Cursor

        cursor = sqlitedb.rawQuery("SELECT * FROM writing WHERE tag = 0;", null)
        if (cursor.moveToFirst()) {
            do {
                gid = cursor.getInt(cursor.getColumnIndex("WId"))
                Log.d("id tag", "${gid}")
                gtitle = cursor.getString(cursor.getColumnIndex("title")).toString()
                gprice = cursor.getString(cursor.getColumnIndex("price")).toString()
                gperson = cursor.getInt(cursor.getColumnIndex("person"))
                gcount = cursor.getInt(cursor.getColumnIndex("count"))

                val vimage = cursor.getBlob(cursor.getColumnIndex("image")) ?: null
                val bitmap = BitmapFactory.decodeByteArray(vimage, 0, vimage!!.size)
                var image = bitmap
                var id = gid
                var title = gtitle
                var price = gprice + "원"
                var person = gperson
                var count = gcount
                var memo = Memo(id, title, price, count, person, image)
                data.add(memo)
            } while (cursor.moveToNext())
        }
        cursor.close()
        sqlitedb.close()

        return data;
    }


    // 워터마크 생성
    fun addWatermark(res: Resources?, source: Bitmap): Bitmap? {
        val w: Int
        val h: Int
        val c: Canvas
        val paint: Paint
        val bmp: Bitmap
        val watermark: Bitmap
        val matrix: Matrix
        val scale: Float
        val r: RectF
        w = source.width
        h = source.height

        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG)

        c = Canvas(bmp)
        c.drawBitmap(source, 0.0f, 0.0f, paint)

        watermark = BitmapFactory.decodeResource(res, R.drawable.soldout)
        scale = (h.toFloat() / watermark.height.toFloat()).toFloat()

        matrix = Matrix()
        matrix.postScale(scale, scale)
        r = RectF(0.0f, 0.0f, watermark.width.toFloat(), watermark.height.toFloat())
        matrix.mapRect(r)
        matrix.postTranslate(w - r.width(), h - r.height())
        c.drawBitmap(watermark, matrix, paint)
        watermark.recycle()
        return bmp
    }

    // 옵션 메뉴 (새로고침)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_groupbuying, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.gb_reload -> {
                val intent = Intent(this, GroupBuying::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}