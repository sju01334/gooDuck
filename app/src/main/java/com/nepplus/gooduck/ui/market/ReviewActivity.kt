package com.nepplus.gooduck.ui.market

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.Dimension
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.ReviewRecyclerAdapter
import com.nepplus.gooduck.databinding.ActivityReviewBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Product
import com.nepplus.gooduck.models.Review
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewActivity : BaseActivity() {

    lateinit var binding : ActivityReviewBinding
    lateinit var mProduct : Product
    lateinit var mReviewAdapter : ReviewRecyclerAdapter

    var mReviewList  = ArrayList<Review>()
    var productId = 0
    var reviewSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review)

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
        subTitleTxt.setTextSize(Dimension.SP, 18F)

        background.setBackgroundColor(getColor(R.color.secondary))

        //        Log.d("리뷰개수22", reviewSize.toString())
//
//        if(reviewSize == 0){
//            binding.emptyLayout.visibility = View.VISIBLE
//            binding.reviewRecyclerView.visibility = View.GONE
//        }else{
//            binding.emptyLayout.visibility = View.GONE
//            binding.reviewRecyclerView.visibility = View.VISIBLE
//        }

        mReviewAdapter = ReviewRecyclerAdapter(mContext, mReviewList)
        binding.reviewRecyclerView.adapter = mReviewAdapter
        binding.reviewRecyclerView.layoutManager = GridLayoutManager(mContext, 2)
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