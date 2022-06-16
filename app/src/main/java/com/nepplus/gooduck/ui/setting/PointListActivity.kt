package com.nepplus.gooduck.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.PaymentRecyclerAdapter
import com.nepplus.gooduck.adapters.PointRecyclerAdapter
import com.nepplus.gooduck.databinding.ActivityPaymentListBinding
import com.nepplus.gooduck.databinding.ActivityPointListBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Payment
import com.nepplus.gooduck.models.Point
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PointListActivity : BaseActivity() {

    lateinit var  binding : ActivityPointListBinding

    lateinit var mPointAdapter : PointRecyclerAdapter
    var mPointList = ArrayList<Point>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_point_list)

        getPointList()
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
        subTitleTxt.text = "포인트 적립내역"


        mPointAdapter = PointRecyclerAdapter(mContext, mPointList)
        binding.pointRecyclerView.adapter = mPointAdapter
        binding.pointRecyclerView.layoutManager = LinearLayoutManager(mContext)




    }

    fun getPointList(){
        apiList.getRequestMyPoint().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mPointList.clear()
                    mPointList.addAll(br.data.point_logs)

                    var total = 0
                    for(item in mPointList){
                        total += item.amount
                    }
                    binding.totalPoint.text = "${total}P"

                    mPointAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }
        })
    }
}