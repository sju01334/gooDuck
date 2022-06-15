package com.nepplus.gooduck.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nepplus.gooduck.R
import com.nepplus.gooduck.api.APIList
import com.nepplus.gooduck.api.ServerApi
import com.nepplus.gooduck.dialog.CustomAlertDialog
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Card
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
    val mProductList: List<Product>
) : RecyclerView.Adapter<MarketDetailRecyclerAdapter.ItemViewHolder>() {

    private lateinit var itemClickListener : OnItemClickListener

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.name)
        val price = itemView.findViewById<TextView>(R.id.price)
        val originalPrice = itemView.findViewById<TextView>(R.id.originalPrice)
        val image = itemView.findViewById<ImageView>(R.id.image)
        val addCartBtn = itemView.findViewById<FloatingActionButton>(R.id.addCartBtn)
        val reviewBtn = itemView.findViewById<TextView>(R.id.reviewBtn)
        val buyBtn = itemView.findViewById<TextView>(R.id.buyBtn)

        val apiList = ServerApi.getRetrofit(mContext).create(APIList::class.java)

        var mCardList = ArrayList<Card>()
        var cardId = 0


        fun createAlert(mCardNickList : ArrayList<String>, productId : Int){
            val alert = CustomAlertDialog(mContext)

            alert.myDialog(object : CustomAlertDialog.ButtonClickListener{
                override fun positiveBtnClicked() {

                    apiList.postRequestPurchaseProduct(
                        productId,
                        cardId
                    ).enqueue(object : Callback<BasicResponse>{
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(response.isSuccessful){
                                val br = response.body()!!
                                Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()
                                alert.dialog.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                        }
                    })

                }

                override fun negativeBtnClicked() {

                }
            })

            alert.binding.titleTxt.text = "구독하기"
            alert.binding.bodyTxt.text = "해당 상품을 구독하시겠습니까?"
            alert.binding.contentEdt1.visibility = View.GONE
            alert.binding.cardLayout.visibility = View.VISIBLE

            val cardAdapter = ArrayAdapter(
                mContext,
                androidx.databinding.library.baseAdapters.R.layout.support_simple_spinner_dropdown_item,
                mCardNickList)
            alert.binding.cardSpinner.adapter = cardAdapter

            alert.binding.cardSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    cardId = mCardList[position].id
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }

        fun getCardList(prorductId : Int){
            apiList.getRequestMyCard().enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        val br = response.body()!!
                        mCardList.addAll(br.data.cards)

                        val mCardNickList = ArrayList<String>()
                        mCardNickList.addAll(br.data.cards.map { it.cardNick })
                        createAlert(mCardNickList, prorductId)

                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }
            })
        }



        fun bind(item: Product) {

            name.text = item.name
            price.text = "${item.price}원"
            originalPrice.text = "${String.format("%.0f",item.price * 1.3)}원"
            originalPrice.paintFlags = originalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
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

            buyBtn.setOnClickListener {
                getCardList(item.id)

            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.list_item_detail, parent, false)
        return ItemViewHolder(row)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mProductList[position])

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return mProductList.size
    }

    interface OnItemClickListener{fun onClick(v : View , position: Int)}

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }







}