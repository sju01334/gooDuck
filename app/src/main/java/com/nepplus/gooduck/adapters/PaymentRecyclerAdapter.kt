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
import com.nepplus.gooduck.models.Payment

class PaymentRecyclerAdapter(
    val mContext: Context,
    val mList: List<Payment>
) : RecyclerView.Adapter<PaymentRecyclerAdapter.ItemViewHolder>() {
    lateinit var binding : ListItemPaymentBinding

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Payment) {
            Glide.with(mContext).load(item.subscription.product.imageUrl).fitCenter().into(binding.image)
            binding.storeName.text = item.subscription.product.store.name
            binding.productName.text = item.subscription.product.name
            binding.priceTxt.text = "${ item.subscription.product.price }원"
            binding.createdAt.text = item.createdAt.substring(2, 10).replace("-",".")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.list_item_payment,
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