package com.nepplus.gooduck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ListItemReplyBinding
import com.nepplus.gooduck.models.Reply

class ReplyRecyclerAdapter(
    val mContext: Context,
    val mList: List<Reply>
) : RecyclerView.Adapter<ReplyRecyclerAdapter.ItemViewHolder>() {
    lateinit var binding : ListItemReplyBinding

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Reply) {
            Glide.with(mContext).load(item.user.profileImg).fitCenter().into(binding.profileImg)
            binding.nickname.text = item.user.nickname
            binding.content.text = item.content
            binding.createdAt.text = item.createdAt.substring(0, 10)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.list_item_reply,
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