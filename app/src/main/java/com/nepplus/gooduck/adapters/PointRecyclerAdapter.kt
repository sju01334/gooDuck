package com.nepplus.gooduck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ListItemPaymentBinding
import com.nepplus.gooduck.databinding.ListItemPointBinding
import com.nepplus.gooduck.models.Payment
import com.nepplus.gooduck.models.Point

class PointRecyclerAdapter(
    val mContext: Context,
    val mList: List<Point>
) : RecyclerView.Adapter<PointRecyclerAdapter.ItemViewHolder>() {
    lateinit var binding : ListItemPointBinding

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Point) {
            binding.pointTxt.text = "+${item.amount}P"
            binding.Txt.text = item.type
            binding.createdAt.text = item.createdAt.substring(2, 10).replace("-",".")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.list_item_point,
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