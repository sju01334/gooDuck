package com.nepplus.gooduck.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.ImageSliderAdapter
import com.nepplus.gooduck.adapters.MarketRecyclerAdapter
import com.nepplus.gooduck.databinding.FragmentMarketBinding
import com.nepplus.gooduck.models.Banner
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Category
import com.nepplus.gooduck.models.SmallCategory
import com.nepplus.gooduck.ui.main.MainActivity
import kotlinx.coroutines.flow.combine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarketFragment  : BaseFragment(){

    lateinit var binding : FragmentMarketBinding

    var mbannerList = ArrayList<Banner>()

    lateinit var mMarketAdapter : MarketRecyclerAdapter
    var mMarketList = ArrayList<Category>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_market, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getImageList()
        getMarketList()
        setupEvents()
        setValues()

    }

    override fun setupEvents() {

    }

    override fun setValues() {


        mMarketAdapter = MarketRecyclerAdapter(mContext, mbannerList, mMarketList)
        binding.bigRecyclerView.adapter = mMarketAdapter
        binding.bigRecyclerView.layoutManager = LinearLayoutManager(mContext)


    }

    fun getImageList(){
        apiList.getRequestBanner().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mbannerList.clear()
                    mbannerList.addAll(br.data.banners)
                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {}
        })
    }

    fun getMarketList(){
        apiList.getRequestAllCategory().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mMarketList.clear()
                    mMarketList.addAll(br.data.categories)
                    mMarketAdapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {}
        })
    }



}