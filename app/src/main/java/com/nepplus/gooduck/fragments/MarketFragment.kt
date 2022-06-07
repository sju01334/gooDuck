package com.nepplus.gooduck.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.ImageSliderAdapter
import com.nepplus.gooduck.databinding.FragmentMarketBinding
import com.nepplus.gooduck.models.Banner
import com.nepplus.gooduck.models.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarketFragment  : BaseFragment(){

    lateinit var binding : FragmentMarketBinding

    lateinit var mSliderAdapter : ImageSliderAdapter
    var mbannerList = ArrayList<Banner>()

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
        setupEvents()
        setValues()

    }

    override fun setupEvents() {

    }

    override fun setValues() {
        mSliderAdapter = ImageSliderAdapter(mContext, mbannerList)
        binding.bannerVeiwPager.adapter = mSliderAdapter
        binding.bannerVeiwPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.bannerVeiwPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

            }
        })

    }

    fun getImageList(){
        apiList.getRequestBanner().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mbannerList.clear()
                    mbannerList.addAll(br.data.banners)
                    mSliderAdapter.notifyDataSetChanged()
                    Log.d("이미지 들어왔니", br.data.banners[0].imgUrl)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })
    }
}