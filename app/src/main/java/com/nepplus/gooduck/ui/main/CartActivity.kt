package com.nepplus.gooduck.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.CartRecyclerAdapter
import com.nepplus.gooduck.databinding.ActivityCartBinding
import com.nepplus.gooduck.dialog.CustomAlertDialog
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Card
import com.nepplus.gooduck.models.Cart
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : BaseActivity () {

    lateinit var binding : ActivityCartBinding

    lateinit var mDetailAdapter : CartRecyclerAdapter
    var mCartList = ArrayList<Cart>()

    var mCardList = ArrayList<Card>()
    val mCardNickList = ArrayList<String>()
    var cardId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_cart)

        getProductData()
        getCardList()
        setValues()
        setupEvents()


    }

    override fun setupEvents() {
        
        mDetailAdapter.setItemClickListener(object : CartRecyclerAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
            }
        })

        binding.totalChk.setOnClickListener {
            if(binding.totalChk.isChecked){
                for( item in mCartList){
                    item.isChecked = true
                }
            }else {
                for( item in mCartList){
                    item.isChecked = false
                }
            }
            mDetailAdapter.notifyDataSetChanged()
        }

        binding.deleteChk.setOnClickListener {
            getProductData()
            val selectedData = mCartList.filter { it.isChecked }.map { it.product.id }

            if(selectedData.isEmpty()){
                Toast.makeText(mContext, "상품을 선택하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val alert = CustomAlertDialog(mContext)
            alert.myDialog(object : CustomAlertDialog.ButtonClickListener{
                override fun positiveBtnClicked() {
                    for( i in selectedData){
                        apiList.deleteRequestMyCartProduct(i).enqueue(object : Callback<BasicResponse>{
                            override fun onResponse(
                                call: Call<BasicResponse>,
                                response: Response<BasicResponse>
                            ) {
                                if(response.isSuccessful){
                                    val br = response.body()!!
                                    Log.d("선택된 장바구니 상품 삭제 완료", br.message)
                                    if(i == selectedData[selectedData.size-1]){
                                        getProductData()
                                        binding.totalChk.isChecked = false
                                        Toast.makeText(mContext, "삭제 완료되었습니다", Toast.LENGTH_SHORT).show()
                                    }
                                }else {
                                    val errorBody = response.errorBody()!!.string()
                                    val jsonObj = JSONObject(errorBody)
                                    val code = jsonObj.getString("code")
                                    val message = jsonObj.getString("message")

                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                                Log.d("호출에러", t.toString())
                            }
                        })
                    }

                    alert.dialog.dismiss()

                }

                override fun negativeBtnClicked() {
                }

            })


            alert.binding.contentEdt1.visibility = View.GONE
            alert.binding.titleTxt.text = "장바구니 삭제"
            alert.binding.bodyTxt.text = "선택된 상품을 삭제하시겠습니까?"
            alert.binding.positiveBtn.setTextColor(ContextCompat.getColor(mContext, R.color.red))
            alert.binding.positiveBtn.setBackgroundResource(R.drawable.r5_red_stroke_1dp)

        }

        binding.buyChk.setOnClickListener {
            getProductData()
            val selectedData = mCartList.filter { it.isChecked }.map { it.product.id }

            if(selectedData.isEmpty()){
                Toast.makeText(mContext, "상품을 선택하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val alert = CustomAlertDialog(mContext)
            alert.myDialog(object : CustomAlertDialog.ButtonClickListener{
                override fun positiveBtnClicked() {
                    for( i in selectedData){
                        apiList.postRequestPurchaseProduct(i, cardId).enqueue(object : Callback<BasicResponse>{
                            override fun onResponse(
                                call: Call<BasicResponse>,
                                response: Response<BasicResponse>
                            ) {
                                if(response.isSuccessful){
                                    val br = response.body()!!

                                }else {
                                    val errorBody = response.errorBody()!!.string()
                                    val jsonObj = JSONObject(errorBody)
                                    val code = jsonObj.getString("code")
                                    val message = jsonObj.getString("message")
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                                Log.d("호출에러", t.toString())
                            }
                        })
                    }
                    for( i  in selectedData){
                        apiList.deleteRequestMyCartProduct(i).enqueue(object : Callback<BasicResponse>{
                            override fun onResponse(
                                call: Call<BasicResponse>,
                                response: Response<BasicResponse>
                            ) {
                                if(response.isSuccessful){
                                    val br = response.body()!!
                                    Log.d("선택된 장바구니 상품 삭제 완료", br.message)
                                    if(i == selectedData[selectedData.size-1]){
                                        getProductData()
                                        binding.totalChk.isChecked = false
                                        Toast.makeText(mContext, "구매 완료되었습니다", Toast.LENGTH_SHORT).show()
                                    }
                                }else {
                                    val errorBody = response.errorBody()!!.string()
                                    val jsonObj = JSONObject(errorBody)
                                    val code = jsonObj.getString("code")
                                    val message = jsonObj.getString("message")

                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                                Log.d("호출에러", t.toString())
                            }
                        })
                    }
                    alert.dialog.dismiss()


                }

                override fun negativeBtnClicked() {
                }

            })

            alert.binding.titleTxt.text = "구독하기"
            alert.binding.bodyTxt.text = "해당 상품을 구독하시겠습니까?"
            alert.binding.contentEdt1.visibility = View.GONE
            alert.binding.cardLayout.visibility = View.VISIBLE

            val cardAdapter = ArrayAdapter(
                mContext,
                androidx.databinding.library.baseAdapters.R.layout.support_simple_spinner_dropdown_item,
                mCardNickList)
            alert.binding.cardSpinner.adapter = cardAdapter

            alert.binding.cardSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    cardId = mCardList[position].id
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }



        }

    }

    override fun setValues() {

        backBtn.visibility = View.VISIBLE
        bagBtn.visibility = View.GONE
        sideBarBtn.visibility = View.GONE
        titleTxt.text = "장바구니"
        titleTxt.setTextSize(Dimension.SP, 18F)

        mDetailAdapter = CartRecyclerAdapter(mContext,  mCartList)
        binding.marketDetailRecyclerView.adapter = mDetailAdapter
        binding.marketDetailRecyclerView.layoutManager = LinearLayoutManager(mContext)


    }

    fun getProductData(){
        apiList.getRequestMyCartList().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mCartList.clear()
                    mCartList.addAll(br.data.carts)
                    Log.d("로그로그",mCartList.size.toString())

                    binding.cnt.text = "(${mCartList.size})"
                    binding.totalPrice.text = "${br.data.carts.sumOf { it.product.price }}원"

                    mDetailAdapter.notifyDataSetChanged()
                }else{
                    val errorBodyStr = response.errorBody()!!.string()
                    val jsonObj = JSONObject(errorBodyStr)
//                    val code = jsonObj.getInt("code")
                    val message = jsonObj.getString("message")
                    Log.d("메세지", message)
                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("호출 실패", t.toString())
            }
        })
    }

    fun getCardList(){
        apiList.getRequestMyCard().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mCardList.addAll(br.data.cards)

                    mCardNickList.addAll(br.data.cards.map { it.cardNick })

                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })
    }
}