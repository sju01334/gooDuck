package com.nepplus.gooduck.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

    private lateinit var itemClickListener : OnItemClickListener

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val productName = itemView.findViewById<TextView>(R.id.productName)
        val reviewName = itemView.findViewById<TextView>(R.id.reviewName)
        val userName = itemView.findViewById<TextView>(R.id.userName)
        val ratingBar = itemView.findViewById<ScaleRatingBar>(R.id.ratingBar)

        val image = itemView.findViewById<ImageView>(R.id.image)

        fun bind(item: Review) {

            Glide.with(itemView.context).load(item.user.profileImg).fitCenter().into(image)
            productName.text = "상품명 : ${item.product.name}"
            if(item.content.length > 40){
                var text = ""
                text += item.content.substring(0, 40)
                text += "....."
                reviewName.text = text
            }else{
                reviewName.text = item.content
            }
            userName.text = item.user.nickname
            ratingBar.rating = item.score

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.list_item_review, parent, false)
        return ItemViewHolder(row)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnItemClickListener{fun onClick(v : View , position: Int)}

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }





}