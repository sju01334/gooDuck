package com.nepplus.gooduck.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ActivityCardAddBinding
import com.nepplus.gooduck.models.BasicResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardAddActivity : BaseActivity() {

    lateinit var binding : ActivityCardAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_card_add)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.addCardBtn.setOnClickListener {

            var cardNum = ""
            cardNum += binding.cardNum1.text.toString()
            cardNum += binding.cardNum2.text.toString()
            cardNum += binding.cardNum3.text.toString()
            cardNum += binding.cardNum4.text.toString()

            val cardNick = binding.cardNickEdt.text.toString()
            val cardDate = binding.cardDateEdt.text.toString()
            val birthday = binding.birthdayEdt.text.toString()
            val password = binding.password.text.toString()

            if (cardNum == ""  || cardNum.length != 16){
                Toast.makeText(mContext, "카드번호를 알맞게 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (cardNick.isEmpty()){
                Toast.makeText(mContext, "카드애칭을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (cardDate.isEmpty()){
                Toast.makeText(mContext, "유효기간을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (birthday.isEmpty()){
                Toast.makeText(mContext, "생년월일을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()){
                Toast.makeText(mContext, "비밀번호 앞 2자리를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            apiList.postRequestAddCard(
                cardNum,
                cardNick,
                cardDate,
                birthday,
                password
            ).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        val br = response.body()!!
                        Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }else {
                        val errorBody = response.errorBody()!!.string()
                        val jsonObj = JSONObject(errorBody)
                        val code = jsonObj.getInt("code")
                        val message = jsonObj.getString("message")
                        if(code == 400){
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                            binding.cardNum1.text.clear()
                            binding.cardNum2.text.clear()
                            binding.cardNum3.text.clear()
                            binding.cardNum4.text.clear()
                            binding.cardNickEdt.text.clear()
                            binding.cardDateEdt.text.clear()
                            binding.birthdayEdt.text.clear()
                            binding.password.text.clear()
                        }
                    }
                }
                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }
            })
        }
    }

    override fun setValues() {


        titleTxt.visibility = View.GONE
        sideBarBtn.visibility = View.GONE
        bagBtn.visibility = View.GONE

        backBtn.visibility = View.VISIBLE
        subTitleTxt.visibility = View.VISIBLE
        subTitleTxt.text = "카드 등록"
        background.setBackgroundColor(getColor(R.color.secondary))

    }

}