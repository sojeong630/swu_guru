package com.example.swu_guru

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swu_guru.databinding.MarketlistLayoutBinding


class ListAdapter: RecyclerView.Adapter<Holder>() {
    var itemList = mutableListOf<ListLayout>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = MarketlistLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val layouts = itemList.get(position)
        holder.setListlayout(layouts)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

class Holder(val binding: MarketlistLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun setListlayout(layouts: ListLayout) {
        binding.marketTitle.text = "${layouts.title}"
        binding.marketCost.text = "${layouts.cost}"
    }

}