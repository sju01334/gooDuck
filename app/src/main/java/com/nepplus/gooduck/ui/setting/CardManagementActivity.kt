package com.nepplus.gooduck.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.CardRecyclerAdapter
import com.nepplus.gooduck.databinding.ActivityCardManagementBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Card
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardManagementActivity : BaseActivity() {

    lateinit var binding : ActivityCardManagementBinding

    lateinit var mCardAdapter : CardRecyclerAdapter
    var mCardList = ArrayList<Card>()

    var cardSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_card_management)
        getCardList()
        setupEvents()
        setValues()
    }

    override fun onResume() {
        super.onResume()
        getCardList()
    }

    override fun setupEvents() {
        binding.addCardBtn.setOnClickListener {
            val myIntent = Intent(mContext, CardAddActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        titleTxt.visibility = View.GONE
        sideBarBtn.visibility = View.GONE
        bagBtn.visibility = View.GONE

        backBtn.visibility = View.VISIBLE
        subTitleTxt.visibility = View.VISIBLE
        subTitleTxt.text = "내 카드 목록"
        background.setBackgroundColor(getColor(R.color.secondary))


    }

    fun getCardList(){
        apiList.getRequestMyCard().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    cardSize = br.data.cards.size
//                    Log.d("리뷰개수", reviewSize.toString())

                    if(cardSize > 0 ){
                        binding.emptyLayout.visibility = View.GONE
                        binding.cardRecyclerView.visibility = View.VISIBLE
                        mCardList.clear()
                        mCardList.addAll(br.data.cards)
                        initAdapters()
                    }else {
                        binding.emptyLayout.visibility = View.VISIBLE
                        binding.cardRecyclerView.visibility = View.GONE
                    }
                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }
        })
    }

    fun initAdapters(){
        mCardAdapter = CardRecyclerAdapter(mContext, mCardList)
        binding.cardRecyclerView.adapter = mCardAdapter
        binding.cardRecyclerView.layoutManager = LinearLayoutManager(mContext)

        mCardAdapter.notifyDataSetChanged()
    }
}