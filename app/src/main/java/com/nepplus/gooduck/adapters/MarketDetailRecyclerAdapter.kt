package com.nepplus.gooduck.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nepplus.gooduck.R
import com.nepplus.gooduck.api.APIList
import com.nepplus.gooduck.api.ServerApi
import com.nepplus.gooduck.dialog.CustomAlertDialog
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Cart
import com.nepplus.gooduck.models.Product
import com.nepplus.gooduck.ui.market.CartActivity
import com.nepplus.gooduck.ui.review.ReviewItemActivity
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

    private lateinit var itemClickListener : OnItemClickListener

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.name)
        val price = itemView.findViewById<TextView>(R.id.price)
        val originalPrice = itemView.findViewById<TextView>(R.id.originalPrice)
        val image = itemView.findViewById<ImageView>(R.id.image)
        val addCartBtn = itemView.findViewById<CardView>(R.id.addCartBtn)
        val reviewBtn = itemView.findViewById<TextView>(R.id.reviewBtn)
        val deleteBtn = itemView.findViewById<TextView>(R.id.deleteBtn)
        val buyBtn = itemView.findViewById<TextView>(R.id.buyBtn)

        val apiList = ServerApi.getRetrofit(mContext).create(APIList::class.java)

        fun bind(item: Product) {

            name.text = item.name
            price.text = "${item.price}원"
            originalPrice.text = "${item.price * 1.3}원"
            Glide.with(mContext).load(item.imageUrl).fitCenter().into(image)


            addCartBtn.setOnClickListener {
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

            reviewBtn.setOnClickListener {
                val myIntent = Intent(mContext, ReviewItemActivity::class.java)
                myIntent.putExtra("product", item)
                mContext.startActivity(myIntent)
            }
        }



        fun bindCart(item : Cart){

            name.text = item.product.name
            price.text = "${item.product.price}원"
            originalPrice.text = "${item.product.price * 1.3}원"
            Glide.with(mContext).load(item.product.imageUrl).fitCenter().into(image)

            addCartBtn.visibility = View.GONE
            deleteBtn.visibility = View.VISIBLE

            deleteBtn.setOnClickListener {

                val alert = CustomAlertDialog(mContext)
                alert.myDialog(object : CustomAlertDialog.ButtonClickListener{
                    override fun positiveBtnClicked() {
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

                                (mContext as CartActivity).getProductData()
                                alert.dialog.dismiss()

                            }

                            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                                Log.d("호출에러", t.toString())
                            }
                        })
                    }

                    override fun negativeBtnClicked() {
                    }

                })

                alert.binding.contentEdt1.visibility = View.GONE
                alert.binding.titleTxt.text = "장바구니 삭제"
                alert.binding.bodyTxt.text = "${item.product.name} 을 삭제하시겠습니까?"
                alert.binding.positiveBtn.setBackgroundResource(R.drawable.r5_red_rectangle_fill)




            }

            reviewBtn.setOnClickListener {
                val myIntent = Intent(mContext, ReviewItemActivity::class.java)
                myIntent.putExtra("product", item.product)
                mContext.startActivity(myIntent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.list_item_detail, parent, false)
        return ItemViewHolder(row)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when (type) {
            "Detail" -> holder.bind(mProductList!![position])
            else-> holder.bindCart(mCartLsit!![position])
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return when (type) {
            "Detail" -> mProductList!!.size
            else-> mCartLsit!!.size
        }
    }

    interface OnItemClickListener{fun onClick(v : View , position: Int)}

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }




}