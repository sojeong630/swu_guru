package com.example.swu_guru

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swu_guru.databinding.ItemRecyclerBinding

class CustomAdapter: RecyclerView.Adapter<Holder2>() {
    var listData = mutableListOf<Memo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder2 {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder2(binding)
    }

    override fun onBindViewHolder(holder: Holder2, position: Int) {
        val memo = listData.get(position)
        holder.setMemo(memo)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}

class Holder2(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            val intent = Intent(binding.root.context, GroupBuyingView::class.java)
            intent.putExtra("intent_id", binding.bid.text)
            binding.root.context.startActivity(intent)
            //Toast.makeText(binding.root.context, "${binding.bid.text}", Toast.LENGTH_SHORT).show()
        }
    }

    fun setMemo(memo: Memo) {
        binding.bid.text = "${memo.id}"
        binding.textTitle.text = "${memo.title}"
        binding.textPrice.text = "${memo.price}"
        binding.textcount.text = "${memo.count}"
        binding.textperson.text = "${memo.person}"
        binding.imageView.setImageBitmap(memo.image)
    }

}