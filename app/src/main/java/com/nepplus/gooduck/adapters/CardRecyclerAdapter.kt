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
import com.nepplus.gooduck.databinding.ListItemCardBinding
import com.nepplus.gooduck.databinding.ListItemReplyBinding
import com.nepplus.gooduck.models.Card
import com.nepplus.gooduck.models.Reply

class CardRecyclerAdapter(
    val mContext: Context,
    val mList: List<Card>
) : RecyclerView.Adapter<CardRecyclerAdapter.ItemViewHolder>() {
    lateinit var binding : ListItemCardBinding

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Card) {
            Glide.with(mContext).load(item.cardLogo).fitCenter().into(binding.LogoImg)
            binding.cardName.text = item.cardName
            binding.cardNick.text = item.cardNick

            var text = ""
            text += item.cardNum.substring(0,4)
            text += " - **** - **** - "
            text += item.cardNum.substring(12, item.cardNum.length)
            binding.cardNum.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.list_item_card,
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