package com.nepplus.gooduck.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ActivityLoginBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.ui.main.MainActivity
import com.nepplus.gooduck.utils.ContextUtil
import com.nepplus.gooduck.utils.GlobalData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    var TAG = "LoginActivity"



    lateinit var binding : ActivityLoginBinding

    /** Naver Login Module Initialize */
    val naverClientId = "szEW1ZoRlpA_LZXuUVEX"
    val naverClientSecret = "U6M4C8R6GJ"
    val naverClientName = "네이버아이디로 로그인"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        NaverIdLoginSDK.initialize(this, naverClientId, naverClientSecret , naverClientName)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.loginBtn.setOnClickListener {
            val inputEmail = binding.emailEdt.text.toString()
            val inputPw = binding.passwordEdt.text.toString()

            apiList.postRequestLogin(inputEmail, inputPw).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        val br = response.body()!!

                        ContextUtil.setLoginToken(mContext, br.data.token)
                        ContextUtil.setAutoLogin(mContext, binding.autoLoginChk.isChecked)
                        GlobalData.loginUser = br.data.user

                        Toast.makeText(mContext, "${GlobalData.loginUser!!.nickname}님 환영합니다", Toast.LENGTH_SHORT).show()

                        val myIntent = Intent(mContext, MainActivity::class.java)
                        startActivity(myIntent)
                        finish()

                    }else{
                        val errorBodyStr = response.errorBody()!!.string()
                        val jsonObj = JSONObject(errorBodyStr)
                        val message = jsonObj.getString("message")

                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }
            })
        }

        binding.signUpBtn.setOnClickListener {
            val myIntent = Intent(mContext, SignupActivity::class.java)
            startActivity(myIntent)

        }

        binding.serachEmail.setOnClickListener {
            val myIntent = Intent(mContext, SearchEmailActivity::class.java)
            startActivity(myIntent)
        }

        binding.searchPW.setOnClickListener {
            val myIntent = Intent(mContext, SearchPasswordActivity::class.java)
            startActivity(myIntent)
        }

        binding.kakaoLoginBtn.setOnClickListener {
            kakaoLogin()
        }

        binding.naverLoginBtn.setOnClickListener {
            naverLogin()
        }
    }

    override fun setValues() {

    }

    fun kakaoLogin(){
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                getKakaoUser()
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(mContext)) {
            UserApiClient.instance.loginWithKakaoTalk(mContext) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(mContext, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    getKakaoUser()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(mContext, callback = callback)
        }
    }

    fun getKakaoUser (){
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}")
                socialLogin("kakao", "${user.id}", "${user.kakaoAccount?.profile?.nickname}")
            }
        }
    }



    fun socialLogin(provider : String, uid : String, nickname : String){
        apiList.postRequestSocialLogin(provider, uid, nickname).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!

                    ContextUtil.setLoginToken(mContext, br.data.token)
                    ContextUtil.setAutoLogin(mContext, binding.autoLoginChk.isChecked)
                    GlobalData.loginUser = br.data.user

                    Toast.makeText(mContext, "${GlobalData.loginUser!!.nickname}님 환영합니다", Toast.LENGTH_SHORT).show()

                    val myIntent = Intent(mContext, MainActivity::class.java)
                    startActivity(myIntent)
                    finish()

                }else{
                    val errorBodyStr = response.errorBody()!!.string()
                    val jsonObj = JSONObject(errorBodyStr)
                    val message = jsonObj.getString("message")

                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })
    }

    fun naverLogin(){
        var naverToken :String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                val userId = response.profile?.id
                val nickName = response.profile?.nickname
                socialLogin("naver", "${userId}", "${nickName}")
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(mContext, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        /** OAuthLoginCallback을 authenticate() 메서드 호출 시 파라미터로 전달하거나 NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다. */
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken = NaverIdLoginSDK.getAccessToken()
//                var naverRefreshToken = NaverIdLoginSDK.getRefreshToken()
//                var naverExpiresAt = NaverIdLoginSDK.getExpiresAt().toString()
//                var naverTokenType = NaverIdLoginSDK.getTokenType()
//                var naverState = NaverIdLoginSDK.getState().toString()

                //로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(profileCallback)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(mContext, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }
}