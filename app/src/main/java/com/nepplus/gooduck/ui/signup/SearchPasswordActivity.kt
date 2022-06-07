package com.nepplus.gooduck.ui.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ActivitySearchEmailBinding
import com.nepplus.gooduck.databinding.ActivitySearchPasswordBinding
import com.nepplus.gooduck.models.BasicResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPasswordActivity : BaseActivity() {

    lateinit var binding : ActivitySearchPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_password)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.searchBtn.setOnClickListener {
            val nickname = binding.nicknameEdt.text.toString()
            val email = binding.emailEdt.text.toString()

            if(nickname.isEmpty()){
                Toast.makeText(mContext, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(email.isEmpty()){
                Toast.makeText(mContext, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                searchEmail(nickname, email)
            }

        }
    }

    override fun setValues() {

    }

    fun searchEmail(nickname : String, email : String){
        apiList.getReqeustFindPw(nickname, email).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    val errorBodyStr = response.errorBody()!!.string()
                    val jsonObj = JSONObject(errorBodyStr)
                    val code = jsonObj.getInt("code")
                    val message = jsonObj.getString("message")

                    if (code == 400) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Log.e("회원가입", "errorCode : ${code}, message : ${message}")
                    }
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                
            }
        })
    }
}