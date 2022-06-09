package com.nepplus.gooduck.ui.market

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Dimension
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.MarketDetailRecyclerAdapter
import com.nepplus.gooduck.databinding.ActivityCartBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Cart
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : BaseActivity () {

    lateinit var binding : ActivityCartBinding

    lateinit var mDetailAdapter : MarketDetailRecyclerAdapter
    var mCartList = ArrayList<Cart>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_cart)

        getProductData()
        setValues()
        setupEvents()


    }

    override fun setupEvents() {
        
        mDetailAdapter.setItemClickListener(object : MarketDetailRecyclerAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
            }
        })

    }

    override fun setValues() {

        backBtn.visibility = View.VISIBLE
        bagBtn.visibility = View.GONE
        sideBarBtn.visibility = View.GONE
        titleTxt.text = "장바구니"
        titleTxt.setTextSize(Dimension.SP, 18F)

        mDetailAdapter = MarketDetailRecyclerAdapter(mContext, null, mCartList, "Cart")
        binding.marketDetailRecyclerView.adapter = mDetailAdapter
        binding.marketDetailRecyclerView.layoutManager = LinearLayoutManager(mContext)


    }

    fun getProductData(){

        apiList.getRequestMyCartList().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mCartList.clear()
                    mCartList.addAll(br.data.carts)
                    mDetailAdapter.notifyDataSetChanged()
                }else{
                    val errorBodyStr = response.errorBody()!!.string()
                    val jsonObj = JSONObject(errorBodyStr)
//                    val code = jsonObj.getInt("code")
                    val message = jsonObj.getString("message")
                    Log.d("메세지", message)
                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("호출 실패", t.toString())
            }
        })
    }
}