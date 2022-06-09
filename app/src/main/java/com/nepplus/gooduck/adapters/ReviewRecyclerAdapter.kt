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
import com.nepplus.gooduck.databinding.ListItemReviewBinding
import com.nepplus.gooduck.models.Review
import com.nepplus.gooduck.models.SmallCategory
import com.nepplus.gooduck.ui.market.MarketDetailActivity

class ReviewRecyclerAdapter(
    val mContext: Context,
    val mList: List<Review>
) : RecyclerView.Adapter<ReviewRecyclerAdapter.ItemViewHolder>() {

    lateinit var binding: ListItemReviewBinding

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Review) {

            Glide.with(itemView.context).load(item.thumImg).fitCenter().into(binding.image)
            binding.price.text = "${item.product.price}원"
            binding.productName.text = item.product.name
            binding.reviewName.text = item.title
            binding.ratingTxt.text = "(${item.score})"
            binding.userName.text = "작성자 : ${item.user.nickname}"
            binding.ratingBar.rating = item.score



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.list_item_review,
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