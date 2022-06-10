package com.nepplus.gooduck.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ItemSmallCategoryBinding
import com.nepplus.gooduck.databinding.ListItemReviewBinding
import com.nepplus.gooduck.models.Review
import com.nepplus.gooduck.models.SmallCategory
import com.nepplus.gooduck.ui.market.MarketDetailActivity
import com.willy.ratingbar.ScaleRatingBar

class ReviewRecyclerAdapter(
    val mContext: Context,
    val mList: List<Review>
) : RecyclerView.Adapter<ReviewRecyclerAdapter.ItemViewHolder>() {


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val ratingTxt = itemView.findViewById<TextView>(R.id.ratingTxt)
        val price = itemView.findViewById<TextView>(R.id.price)
        val productName = itemView.findViewById<TextView>(R.id.productName)
        val reviewName = itemView.findViewById<TextView>(R.id.reviewName)
        val userName = itemView.findViewById<TextView>(R.id.userName)
        val ratingBar = itemView.findViewById<ScaleRatingBar>(R.id.ratingBar)

        val image = itemView.findViewById<ImageView>(R.id.image)

        fun bind(item: Review) {

            Glide.with(itemView.context).load(item.thumImg).fitCenter().into(image)
            price.text = "${item.product.price}원"
            productName.text = item.product.name
            reviewName.text = item.title
            ratingTxt.text = "(${item.score})"
            userName.text = "작성자 : ${item.user.nickname}"
            ratingBar.rating = item.score

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.list_item_review, parent, false)
        return ItemViewHolder(row)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }



}