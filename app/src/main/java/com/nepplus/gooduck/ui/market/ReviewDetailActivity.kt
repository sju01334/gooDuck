package com.nepplus.gooduck.ui.market

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Dimension
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.ReplyRecyclerAdapter
import com.nepplus.gooduck.databinding.ActivityReviewDetailBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Reply
import com.nepplus.gooduck.models.Review
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewDetailActivity : BaseActivity() {

    lateinit var binding : ActivityReviewDetailBinding

    lateinit var mReveiw : Review
    lateinit var mReplyAdatper : ReplyRecyclerAdapter

    var mReplyList = ArrayList<Reply>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_detail)

        mReveiw = intent.getSerializableExtra("review") as Review
        getReplyList()

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.replyAddBtn.setOnClickListener {

            if(binding.replyEdt.text.isEmpty()){
                Toast.makeText(mContext, "댓글을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            apiList.postRequestAddReply(mReveiw.id, binding.replyEdt.text.toString()).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        val br = response.body()!!
//                        Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()
                        binding.replyEdt.text.clear()
                        binding.replyRecyclerView.visibility = View.VISIBLE
                        getReplyList()
                    }
                }
                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                }
            })
        }

    }

    override fun setValues() {

        titleTxt.text = "${mReveiw.user.nickname}님의 리뷰"
        titleTxt.setTextSize(Dimension.SP, 18F)
        sideBarBtn.visibility = View.GONE
        bagBtn.visibility = View.GONE
        backBtn.visibility = View.VISIBLE

        Glide.with(mContext).load(mReveiw.user.profileImg).fitCenter().into(binding.profileImg)
        binding.nickname.text = mReveiw.user.nickname
        binding.ratingBar.rating = mReveiw.score
        binding.createdAt.text = mReveiw.createdAt.substring(0, 10)
        binding.productName.text = mReveiw.product.name
        binding.title.text = mReveiw.title
        binding.content.text = mReveiw.content
        Glide.with(mContext).load(mReveiw.thumImg).fitCenter().into(binding.thumbImg)
        for( tag in mReveiw.tags){
            binding.chipGroup.addView(Chip(this).apply {
                text = tag.tag
            })
        }
        if(mReveiw.user.provider != "default"){
            binding.socialImg.visibility = View.VISIBLE
            when(mReveiw.user.provider){
                "kakao" -> binding.socialImg.setImageResource(R.drawable.kakao_login_icon)
                "facebook" -> binding.socialImg.setImageResource(R.drawable.facebook_login_icon)
                "naver" -> binding.socialImg.setImageResource(R.drawable.naver_icon)
                "google" -> binding.socialImg.setImageResource(R.drawable.google_login_icon)
            }
        }

        mReplyAdatper = ReplyRecyclerAdapter(mContext, mReplyList)
        binding.replyRecyclerView.adapter = mReplyAdatper
        binding.replyRecyclerView.layoutManager = LinearLayoutManager(mContext)


    }

    fun getReplyList(){

        apiList.getRequestReviewReply(mReveiw.id).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br= response.body()!!
                    mReplyList.clear()
                    mReplyList.addAll( br.data.replies)
                    if(mReplyList.size != 0){
                        binding.replyRecyclerView.visibility = View.VISIBLE
                        mReplyAdatper.notifyDataSetChanged()
                    }
                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })
    }


}