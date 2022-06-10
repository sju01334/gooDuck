package com.nepplus.gooduck.ui.market

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ActivityReviewAddBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Category
import com.nepplus.gooduck.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewAddActivity : BaseActivity() {

    lateinit var binding : ActivityReviewAddBinding

    lateinit var mProduct : Product

    var mCategoryList = ArrayList<String>()

    var categoryId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_add)

        getSmallCategory()
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
        subTitleTxt.text = "리뷰 등록"


        val categoryAdapter =ArrayAdapter(mContext, R.layout.item_spinner, mCategoryList)
        binding.categorySpinner.adapter = categoryAdapter

        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                Toast.makeText(mContext, "위치 ${position}, 아이디? ${id}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }



    }

    fun getSmallCategory(){
        apiList.getRequestSmallCategory().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mCategoryList.clear()
                    mCategoryList.addAll(br.data.categories.map { it.name })
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })
    }

    fun getProduct(){

    }
}