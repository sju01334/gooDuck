package com.nepplus.gooduck.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.MarketRecyclerAdapter
import com.nepplus.gooduck.adapters.ReviewRecyclerAdapter
import com.nepplus.gooduck.databinding.FragmentReviewBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Review
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewFragment  : BaseFragment(){

    lateinit var binding : FragmentReviewBinding

    lateinit var mReviewAdapter : ReviewRecyclerAdapter
    var mReviewList = ArrayList<Review>()

    var reviewSize = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()
        setValues()


    }

    override fun onResume() {
        super.onResume()
        getAllReviewList()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

//        Log.d("리뷰개수22", reviewSize.toString())
//
//        if(reviewSize == 0){
//            binding.emptyLayout.visibility = View.VISIBLE
//            binding.reviewRecyclerView.visibility = View.GONE
//        }else{
//            binding.emptyLayout.visibility = View.GONE
//            binding.reviewRecyclerView.visibility = View.VISIBLE
//        }


    }

    fun getAllReviewList(){
        apiList.getRequestAllReview().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    reviewSize = br.data.reviews.size
                    Log.d("리뷰개수", reviewSize.toString())

                    if(reviewSize > 0 ){

                        if(reviewSize != 0){
                            mReviewList.clear()
                        }
                        mReviewList.addAll(br.data.reviews)
                        initAdapters()
                    }
                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {Log.d("호출 실패", t.toString())}
        })
    }

    fun initAdapters(){
        mReviewAdapter = ReviewRecyclerAdapter(mContext, mReviewList, 0)
        binding.reviewRecyclerView.adapter = mReviewAdapter
        binding.reviewRecyclerView.layoutManager = GridLayoutManager(mContext, 2)

        mReviewAdapter.notifyDataSetChanged()
    }
}