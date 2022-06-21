package com.nepplus.gooduck.ui.main

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.MarketDetailRecyclerAdapter
import com.nepplus.gooduck.databinding.ActivitySearchBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Product
import com.nepplus.gooduck.models.SmallCategory
import com.nepplus.gooduck.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : BaseActivity() {

    lateinit var binding : ActivitySearchBinding
    lateinit var mDetailAdapter : MarketDetailRecyclerAdapter

    val mSmallCategoryList = ArrayList<SmallCategory>()
    var mProductList = ArrayList<Product>()
    var mProduct = ArrayList<Product>()

    var recentList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)



        getCategoryList()

        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.searchBtn.setOnClickListener {
            val searchEdt = binding.searchEdt.text.toString()

            if(recentList.size >= 10){
                recentList.removeAt(0)
            }

            if(recentList.contains(searchEdt)){
                recentList.remove(searchEdt)
                recentList.add(searchEdt)
            }else{
                recentList.add(searchEdt)
            }

            recentList.reverse()
            ContextUtil.setRecentSearch(mContext, recentList)
            recentList.reverse()

            if(searchEdt.isEmpty()){
                Toast.makeText(mContext, "검색어를 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val list = mSmallCategoryList.filter { it.name == searchEdt }
            getSearchedProduct(searchEdt)



        }

        binding.searchEdt.addTextChangedListener {
            if(it.toString().isEmpty()){
                binding.emptyLayout.visibility = View.VISIBLE
                binding.recentSearchLayout.visibility = View.VISIBLE
                binding.searchListRecyclerView.visibility = View.GONE
                binding.recentChipGroup.removeAllViews()
                recentList.reverse()
//                Log.d("리스트", recentList.toString())
                for( tag in recentList){
                    binding.recentChipGroup.addView(Chip(this).apply {
                        text = tag
                        setOnClickListener {
                            getSearchedProduct(text.toString())
                            binding.searchEdt.setText(text.toString())

                        }
                    })
                }
                recentList.reverse()
            }
        }


        binding.eraseSearchList.setOnClickListener {
            ContextUtil.clearTag(mContext, "RECENT_SEARCH")
            recentList = ContextUtil.getRecentSearch(mContext)!!
            binding.recentChipGroup.removeAllViews()
            binding.recentSearchLayout.visibility = View.GONE
        }

        binding.searchEdt.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.searchEdt.getRight() - binding.searchEdt.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    binding.searchEdt.text.clear()
                    return@OnTouchListener true
                }
            }
            false
        })

        binding.backBtn.setOnClickListener { finish() }



    }

    override fun setValues() {


        mDetailAdapter = MarketDetailRecyclerAdapter(mContext, mProduct)
        binding.searchListRecyclerView.adapter = mDetailAdapter
        binding.searchListRecyclerView.layoutManager = GridLayoutManager(mContext,2)


        recentList = ContextUtil.getRecentSearch(mContext)!!
        if(recentList.isEmpty()){
            binding.recentSearchLayout.visibility = View.GONE
        }

        binding.recentChipGroup.removeAllViews()
        for( tag in recentList){
            binding.recentChipGroup.addView(Chip(this).apply {
                text = tag
                setOnClickListener {
                    getSearchedProduct(text.toString())
                    binding.searchEdt.setText(text.toString())
                }
            })
        }
        recentList.reverse()

        val stringList = listOf("스팸", "호빵", "왕란", "콘푸로스트", "딸기")
        for( tag in stringList){
            binding.popularChipGroup.addView(Chip(this).apply {
                text = tag
                setOnClickListener {
                    getSearchedProduct(text.toString())
                    binding.searchEdt.setText(text.toString())
                    if(recentList.contains(text.toString())){
                        recentList.remove(text.toString())
                        recentList.add(text.toString())
                    }else{
                        recentList.add(text.toString())
                    }
                    recentList.reverse()
                    ContextUtil.setRecentSearch(mContext, recentList)
                    recentList.reverse()

                }
            })
        }


    }

    fun getCategoryList(){
        apiList.getRequestAllCategory().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mSmallCategoryList.clear()
                    mSmallCategoryList.addAll(br.data.categories.map { it.smallCategories}.flatten())
                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }
        })
    }

    fun getSearchedProduct(item : String){
        for(i in 1..mSmallCategoryList.size){
            apiList.getRequestProducts(i).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                    if(response.isSuccessful){
                        val br = response.body()!!
                        mProductList.clear()
                        mProductList.addAll(br.data.products)
                        val ll = mProductList.filter { it.name == item }
                        if(ll.isNotEmpty()){
                            mProduct.clear()
                            mProduct.addAll(ll)
                            mDetailAdapter.notifyDataSetChanged()
//                            Log.d("mProductList", mProduct.toString())
                            binding.emptyLayout.visibility = View.GONE
                            binding.searchListRecyclerView.visibility = View.VISIBLE
                            return
                        }else{
//                                    if(i == mSmallCategoryList.size){
//                                        Toast.makeText(mContext, "해당 상품이 존재하지 않습니다", Toast.LENGTH_SHORT).show()
//                                    }
                        }



                    }
                }
                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }
            })

        }
    }

}