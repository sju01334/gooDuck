package com.nepplus.gooduck.ui.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.ReviewRecyclerAdapter
import com.nepplus.gooduck.databinding.ActivityMyReviewListBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Review
import com.nepplus.gooduck.ui.review.ReviewAddActivity
import com.nepplus.gooduck.ui.review.ReviewDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyReviewListActivity : BaseActivity() {

    lateinit var binding : ActivityMyReviewListBinding

    lateinit var mReviewAdapter : ReviewRecyclerAdapter
    var mReviewList = ArrayList<Review>()

    var reviewSize  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_review_list)
        getMyReviewList()
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.addReviewBtn.setOnClickListener {
            val myIntent = Intent(mContext , ReviewAddActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        getMyReviewList()
    }

    override fun setValues() {
        titleTxt.visibility = View.GONE
        sideBarBtn.visibility = View.GONE
        bagBtn.visibility = View.GONE

        backBtn.visibility = View.VISIBLE
        subTitleTxt.visibility = View.VISIBLE
        subTitleTxt.text = "내 리뷰 목록"

        initAdapters()
    }

    fun getMyReviewList(){
        apiList.getRequestMyReview().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    reviewSize = br.data.reviews.size

                    if(reviewSize > 0 ){
                        binding.emptyLayout.visibility = View.GONE
                        binding.reviewRecyclerView.visibility = View.VISIBLE
                        mReviewList.clear()
                        mReviewList.addAll(br.data.reviews)
//                        initAdapters()
                    }
                    else {
                        binding.emptyLayout.visibility = View.VISIBLE
                        binding.reviewRecyclerView.visibility = View.GONE
                    }
                    mReviewAdapter.notifyDataSetChanged()


                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("호출 실패", t.toString())}
        })
    }

    fun initAdapters(){
        mReviewAdapter = ReviewRecyclerAdapter(mContext, mReviewList)
        binding.reviewRecyclerView.adapter = mReviewAdapter
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(mContext)



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