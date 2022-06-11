package com.nepplus.gooduck.ui.market

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.Dimension
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ActivityReviewDetailBinding
import com.nepplus.gooduck.models.Review
import com.nepplus.gooduck.models.Tag

class ReviewDetailActivity : BaseActivity() {

    lateinit var binding : ActivityReviewDetailBinding

    lateinit var mReveiw : Review

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_detail)

        mReveiw = intent.getSerializableExtra("review") as Review

        setupEvents()
        setValues()


    }

    override fun setupEvents() {

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

    }
}