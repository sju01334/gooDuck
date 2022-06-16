package com.nepplus.gooduck.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.PaymentRecyclerAdapter
import com.nepplus.gooduck.adapters.ReviewRecyclerAdapter
import com.nepplus.gooduck.databinding.ActivityPaymentListBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Payment
import com.nepplus.gooduck.ui.review.ReviewDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentListActivity : BaseActivity() {

    lateinit var  binding : ActivityPaymentListBinding

    lateinit var mPaymentAdapter : PaymentRecyclerAdapter
    var mPaymentList = ArrayList<Payment>()

    var paymentSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_list)

        getPaymentList()
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
        subTitleTxt.text = "내 구매 목록"

        mPaymentAdapter = PaymentRecyclerAdapter(mContext, mPaymentList)
        binding.purchaseRecyclerView.adapter = mPaymentAdapter
        binding.purchaseRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }

    fun getPaymentList(){
        apiList.getRequestMyPayment().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
               if(response.isSuccessful){
                   val br = response.body()!!
                   paymentSize = br.data.payments.size

                   if(paymentSize > 0 ){
                       binding.emptyLayout.visibility = View.GONE
                       binding.purchaseRecyclerView.visibility = View.VISIBLE
                       mPaymentList.clear()
                       mPaymentList.addAll(br.data.payments)
                   }
                   else {
                       binding.emptyLayout.visibility = View.VISIBLE
                       binding.purchaseRecyclerView.visibility = View.GONE
                   }

                   mPaymentAdapter.notifyDataSetChanged()


               }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })
    }


}