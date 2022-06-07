package com.nepplus.gooduck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ItemSliderBinding
import com.nepplus.gooduck.models.Banner

class ImageSliderAdapter(
    val mContext : Context,
    val mList : List<Banner>
) : RecyclerView.Adapter<ImageSliderAdapter.ItemViewHolder>() {

    lateinit var binding : ItemSliderBinding

    inner class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(item : Banner){
            Glide.with(mContext).load(item.imgUrl).into(binding.imageSlider)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_slider, parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}