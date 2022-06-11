package com.nepplus.gooduck.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.ReviewRecyclerAdapter
import com.nepplus.gooduck.databinding.FragmentReviewBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Review
import com.nepplus.gooduck.ui.market.ReviewAddActivity
import com.nepplus.gooduck.ui.market.ReviewDetailActivity
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

        binding.faBtn.setOnClickListener {
            val myIntent = Intent(mContext , ReviewAddActivity::class.java)
            startActivity(myIntent)
        }

    }

    override fun setValues() {

    }

    fun getAllReviewList(){
        apiList.getRequestAllReview().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    reviewSize = br.data.reviews.size
//                    Log.d("리뷰개수", reviewSize.toString())

                    binding.reviewCnt.text = "${reviewSize}개의 리뷰"
                    if(reviewSize > 0 ){
                        binding.emptyLayout.visibility = View.GONE
                        binding.reviewRecyclerView.visibility = View.VISIBLE
                        if(reviewSize != 0){
                            mReviewList.clear()
                        }
                        mReviewList.addAll(br.data.reviews)
                        initAdapters()
                    }
                    else {
                        binding.emptyLayout.visibility = View.VISIBLE
                        binding.reviewRecyclerView.visibility = View.GONE
                    }

                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {Log.d("호출 실패", t.toString())}
        })
    }

    fun initAdapters(){
        mReviewAdapter = ReviewRecyclerAdapter(mContext, mReviewList)
        binding.reviewRecyclerView.adapter = mReviewAdapter
        binding.reviewRecyclerView.layoutManager = GridLayoutManager(mContext, 2)

        mReviewAdapter.notifyDataSetChanged()

        mReviewAdapter.setItemClickListener(object : ReviewRecyclerAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val myIntent = Intent(mContext, ReviewDetailActivity::class.java)
                myIntent.putExtra("review", mReviewList[position])
                startActivity(myIntent)
//                Toast.makeText(mContext, "${position}번쨰 선택", Toast.LENGTH_SHORT).show()
            }
        })
    }
}