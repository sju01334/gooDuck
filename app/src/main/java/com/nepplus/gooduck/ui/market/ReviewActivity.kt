package com.nepplus.gooduck.ui.market

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ActivityReviewBinding

class ReviewActivity : BaseActivity() {

    lateinit var binding : ActivityReviewBinding

    var productId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review)

//        productId = intent.getIntExtra("product_id")
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
        subTitleTxt.text = "리뷰"
    }
}