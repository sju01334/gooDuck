package com.nepplus.gooduck.ui.review

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.ReviewRecyclerAdapter
import com.nepplus.gooduck.databinding.ActivityReviewItemBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Product
import com.nepplus.gooduck.models.Review
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewItemActivity : BaseActivity() {

    lateinit var binding : ActivityReviewItemBinding
    lateinit var mProduct : Product
    lateinit var mReviewAdapter : ReviewRecyclerAdapter

    var mReviewList  = ArrayList<Review>()
    var productId = 0
    var reviewSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_item)

        mProduct = intent.getSerializableExtra("product") as Product
        productId = mProduct.id

        getAllReviewList()
        setupEvents()
        setValues()
    }

    override fun setupEvents() {


    }

    override fun setValues() {
        titleTxt.visibility = View.GONE
        sideBarBtn.visibility = View.GONE
        bagBtn.visibility = View.GONE

        backBtn.visibility = View.VISIBLE
        subTitleTxt.visibility = View.VISIBLE
        subTitleTxt.text = "${mProduct.name} 리뷰"

        background.setBackgroundColor(getColor(R.color.secondary))

        mReviewAdapter = ReviewRecyclerAdapter(mContext, mReviewList)
        binding.reviewRecyclerView.adapter = mReviewAdapter
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(mContext)

        mReviewAdapter.setItemClickListener(object : ReviewRecyclerAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val myIntent = Intent(mContext, ReviewDetailActivity::class.java)
                myIntent.putExtra("review", mReviewList[position])
                startActivity(myIntent)
            }
        })
    }

    fun getAllReviewList(){
        apiList.getRequestAllReview().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mReviewList.clear()
                    mReviewList.addAll(
                        br.data.reviews.filter {
                            it.productId == productId
                        } as ArrayList<Review>)
                    mReviewAdapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("호출 실패", t.toString())}
        })
    }





}
