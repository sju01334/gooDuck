package com.nepplus.gooduck.ui.market

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Dimension
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.MarketDetailRecyclerAdapter
import com.nepplus.gooduck.databinding.ActivityMarketDetailBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Product
import com.nepplus.gooduck.models.SmallCategory
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarketDetailActivity : BaseActivity () {

    lateinit var binding : ActivityMarketDetailBinding

    lateinit var sCategories : SmallCategory

    lateinit var mDetailAdapter : MarketDetailRecyclerAdapter
    var mProductList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_market_detail)


        sCategories = intent.getSerializableExtra("sCategories") as SmallCategory
        getProductData()
        setValues()
        setupEvents()


    }

    override fun setupEvents() {

        mDetailAdapter.setItemClickListener(object : MarketDetailRecyclerAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Toast.makeText(mContext, position.toString(), Toast.LENGTH_SHORT).show()
            }
        })


    }

    override fun setValues() {
        backBtn.visibility = View.VISIBLE
        sideBarBtn.visibility = View.GONE
        titleTxt.text = sCategories.name
        titleTxt.setTextSize(Dimension.SP, 18F)

        mDetailAdapter = MarketDetailRecyclerAdapter(mContext, mProductList)
        binding.marketDetailRecyclerView.adapter = mDetailAdapter
        binding.marketDetailRecyclerView.layoutManager = GridLayoutManager(mContext,2)

    }

    fun getProductData(){

        apiList.getRequestProducts(sCategories.id).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mProductList.clear()
                    mProductList.addAll(br.data.products)
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