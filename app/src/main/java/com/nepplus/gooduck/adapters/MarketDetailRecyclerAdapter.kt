package com.nepplus.gooduck.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ItemSmallCategoryBinding
import com.nepplus.gooduck.databinding.ListItemDetailBinding
import com.nepplus.gooduck.models.Product
import com.nepplus.gooduck.models.SmallCategory
import com.nepplus.gooduck.ui.market.MarketDetailActivity

class MarketDetailRecyclerAdapter(
    val mContext: Context,
    val mList: List<Product>
) : RecyclerView.Adapter<MarketDetailRecyclerAdapter.ItemViewHolder>() {

    lateinit var binding: ListItemDetailBinding

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Product) {
            binding.name.text = item.name
            binding.price.text = "${item.price}원"
            binding.originalPrice.text = "${item.price * 1.3}원"
            Glide.with(mContext).load(item.imageUrl).fitCenter().into(binding.image)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.list_item_detail, parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }



}