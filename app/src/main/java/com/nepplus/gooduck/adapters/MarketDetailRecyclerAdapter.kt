package com.nepplus.gooduck.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nepplus.gooduck.R
import com.nepplus.gooduck.api.APIList
import com.nepplus.gooduck.api.ServerApi
import com.nepplus.gooduck.databinding.ListItemDetailBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Cart
import com.nepplus.gooduck.models.Product
import com.nepplus.gooduck.ui.market.CartActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarketDetailRecyclerAdapter(
    val mContext: Context,
    val mProductList: List<Product>?,
    val mCartLsit: List<Cart>?,
    val type : String
) : RecyclerView.Adapter<MarketDetailRecyclerAdapter.ItemViewHolder>() {

    lateinit var binding: ListItemDetailBinding

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val apiList = ServerApi.getRetrofit(mContext).create(APIList::class.java)

        fun bind(item: Product) {

            binding.name.text = item.name
            binding.price.text = "${item.price}원"
            binding.originalPrice.text = "${item.price * 1.3}원"
            Glide.with(mContext).load(item.imageUrl).fitCenter().into(binding.image)

            binding.addCartBtn.setOnClickListener {
                apiList.postRequestAddCart(item.id).enqueue(object : Callback<BasicResponse>{
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if(response.isSuccessful){
                            val br = response.body()!!
                            Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()

                        }else {
                            val errorBody = response.errorBody()!!.string()
                            val jsonObj = JSONObject(errorBody)
                            val code = jsonObj.getString("code")
                            val message = jsonObj.getString("message")

                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        Log.d("호출에러", t.toString())
                    }
                })

            }
        }

        fun bindCart(item : Cart){

            binding.name.text = item.product.name
            binding.price.text = "${item.product.price}원"
            binding.originalPrice.text = "${item.product.price * 1.3}원"
            Glide.with(mContext).load(item.product.imageUrl).fitCenter().into(binding.image)

            binding.addCartBtn.visibility = View.GONE
            binding.deleteBtn.visibility = View.VISIBLE

            binding.deleteBtn.setOnClickListener {
                apiList.deleteRequestMyCartProduct(item.product.id).enqueue(object : Callback<BasicResponse>{
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if(response.isSuccessful){
                            val br = response.body()!!
                            Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()

                        }else {
                            val errorBody = response.errorBody()!!.string()
                            val jsonObj = JSONObject(errorBody)
                            val code = jsonObj.getString("code")
                            val message = jsonObj.getString("message")

                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        Log.d("호출에러", t.toString())
                    }
                })

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.list_item_detail, parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when (type) {
            "Detail" -> holder.bind(mProductList!![position])
            else-> holder.bindCart(mCartLsit!![position])
        }
    }

    override fun getItemCount(): Int {
        return when (type) {
            "Detail" -> mProductList!!.size
            else-> mCartLsit!!.size
        }
    }



}