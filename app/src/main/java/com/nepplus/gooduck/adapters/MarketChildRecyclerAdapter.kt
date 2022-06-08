package com.nepplus.gooduck.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ItemSmallCategoryBinding
import com.nepplus.gooduck.models.Category
import com.nepplus.gooduck.models.SmallCategory

class MarketChildRecyclerAdapter(
    val mContext: Context,
    val mList: List<SmallCategory>
) : RecyclerView.Adapter<MarketChildRecyclerAdapter.ItemViewHolder>() {

    lateinit var binding: ItemSmallCategoryBinding

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SmallCategory) {
            binding.smallCategory.text = item.name
            Glide.with(mContext).load(item.imageUrl).into(binding.smallImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.item_small_category,
            parent,
            false
        )
        return ItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }



}