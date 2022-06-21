package com.nepplus.gooduck.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.FragmentSettingBinding
import com.nepplus.gooduck.dialog.CustomAlertDialog
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.ui.signup.LoginActivity
import com.nepplus.gooduck.ui.setting.CardManagementActivity
import com.nepplus.gooduck.ui.setting.MyReviewListActivity
import com.nepplus.gooduck.ui.setting.PaymentListActivity
import com.nepplus.gooduck.ui.setting.PointListActivity
import com.nepplus.gooduck.utils.ContextUtil
import com.nepplus.gooduck.utils.GlobalData
import com.nepplus.gooduck.utils.URIPathHelper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SettingFragment  : BaseFragment(){

    lateinit var binding : FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.changeProfileImg.setOnClickListener {

            val pl = object : PermissionListener {
                override fun onPermissionGranted() {
                    val myIntent = Intent()
                    myIntent.action = Intent.ACTION_PICK
                    myIntent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
                    startForResult.launch(myIntent)
                }
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                }
            }
//            권한이 OK 일떄
            TedPermission.create()
                .setPermissionListener(pl)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check()
        }

        val ocl = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val type = p0!!.tag.toString()


                val alert = CustomAlertDialog(mContext)

                alert.myDialog(object : CustomAlertDialog.ButtonClickListener{
                    override fun positiveBtnClicked() {
                        val changedEdt = alert.binding.contentEdt1.text.toString()

                        if(alert.binding.contentEdt1.text.toString().isEmpty()){
                            Toast.makeText(mContext, "빈칸을 입력하세요", Toast.LENGTH_SHORT).show()
                            return
                        }

                        var password: String? = null

                        if( type == "password"){
                            password = alert.binding.contentEdt2.text.toString()

                            if(password == null){
                                Toast.makeText(mContext, "빈칸을 입력하세요", Toast.LENGTH_SHORT).show()
                                return
                            }
                        }

                        apiList.patchRequestEditUserInfo(type, changedEdt, password)
                            .enqueue(object : Callback<BasicResponse> {
                                override fun onResponse(
                                    call: Call<BasicResponse>,
                                    response: Response<BasicResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val br = response.body()!!
                                        GlobalData.loginUser = br.data.user
                                        setUserData()
                                        Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()

                                    } else {
                                        val errorBodyStr = response.errorBody()!!.string()
                                        val jsonObj = JSONObject(errorBodyStr)
                                        val message = jsonObj.getString("message")
                                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                                    }
                                    alert.dialog.dismiss()
                                }

                                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                                }
                            })
                    }
                    override fun negativeBtnClicked() {
                    }
                })

                alert.binding.bodyTxt.visibility = View.GONE



                when (type) {
                    "nickname" -> {
                        alert.binding.titleTxt.text = "닉네임 변경"
                        alert.binding.contentEdt1.hint = "변경할 닉네임을 입력하세요"
                        alert.binding.contentEdt1.inputType = InputType.TYPE_CLASS_TEXT
                    }
                    "receive_email" -> {
                        alert.binding.titleTxt.text = "이메일 변경"
                        alert.binding.contentEdt1.hint = "변경할 이메일을 입력하세요"
                        alert.binding.contentEdt1.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    }
                    "password" -> {
                        alert.binding.titleTxt.text = "비밀번호 변경"
                        alert.binding.contentEdt1.hint = "변경할 비밀번호를 입력하세요"
                        alert.binding.contentEdt1.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                        alert.binding.contentEdt2.visibility = View.VISIBLE
                        alert.binding.contentEdt2.hint = "현재 비밀번호를 입력하세요"
                        alert.binding.contentEdt2.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD


                    }
                    "phone" -> {
                        alert.binding.titleTxt.text = "전화번호 변경"
                        alert.binding.contentEdt1.hint = "변경할 전화번호를 입력하세요"
                        alert.binding.contentEdt1.inputType = InputType.TYPE_CLASS_PHONE
                    }
                }





            }
        }

        binding.changeNickLayout.setOnClickListener(ocl)
        binding.changeEmailLayout.setOnClickListener(ocl)
        binding.changePhoneLayout.setOnClickListener(ocl)
        binding.changePwLayout.setOnClickListener(ocl)

        //        로그아웃
        binding.logoutBtn.setOnClickListener {
            val alert = CustomAlertDialog(mContext)
            alert.myDialog(object : CustomAlertDialog.ButtonClickListener{
                override fun positiveBtnClicked() {
                    if(GlobalData.loginUser!!.provider == "naver"){
                        NaverIdLoginSDK.logout()
                        startNaverDeleteToken()
                    }
                    ContextUtil.clear(mContext)
                    GlobalData.loginUser = null
                    val myIntent = Intent(mContext, LoginActivity::class.java)
                    myIntent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    alert.dialog.dismiss()
                    startActivity(myIntent)
                }
                override fun negativeBtnClicked() {
                }
            })

            alert.binding.titleTxt.text = "로그 아웃"
            alert.binding.bodyTxt.text = "정말 로그아웃 하시겠습니까 ?"
            alert.binding.contentEdt1.visibility = View.GONE
            alert.binding.positiveBtn.setTextColor(ContextCompat.getColor(mContext, R.color.red))
            alert.binding.positiveBtn.setBackgroundResource(R.drawable.r5_red_stroke_1dp)
        }

        binding.manageCardLayout.setOnClickListener{
            val myIntent = Intent(mContext, CardManagementActivity::class.java)
            startActivity(myIntent)
        }

        binding.manageReviewLayout.setOnClickListener{
            val myIntent = Intent(mContext, MyReviewListActivity::class.java)
            startActivity(myIntent)
        }

        binding.manageBuyLayout.setOnClickListener {
            val myIntent = Intent(mContext, PaymentListActivity::class.java)
            startActivity(myIntent)
        }

        binding.managePointLayout.setOnClickListener {
            val myIntent = Intent(mContext, PointListActivity::class.java)
            startActivity(myIntent)
        }

        binding.byebyeBtn.setOnClickListener{
            val alert = CustomAlertDialog(mContext)
            alert.myDialog(object : CustomAlertDialog.ButtonClickListener{
                override fun positiveBtnClicked() {
                    val text = alert.binding.contentEdt1.text.toString()
                    if(text.isEmpty()){
                        Toast.makeText(mContext, "빈칸을 입력해주세요", Toast.LENGTH_SHORT).show()
                    }else {
                        apiList.deleteRequestUser(text).enqueue(object : Callback<BasicResponse>{
                            override fun onResponse(
                                call: Call<BasicResponse>,
                                response: Response<BasicResponse>
                            ) {
                                if(response.isSuccessful){
                                    val br = response.body()!!
                                    Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()
                                    ContextUtil.clear(mContext)
                                    GlobalData.loginUser = null
                                    val myIntent = Intent(mContext, LoginActivity::class.java)
                                    myIntent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(myIntent)
                                }else{
                                    val errorBody =response.errorBody()!!.string()
                                    val jsonObj = JSONObject(errorBody)
                                    val code = jsonObj.getInt("code")
                                    val message = jsonObj.getString("message")
                                    if(code == 400){
                                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                                alert.dialog.dismiss()
                            }
                            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                            }
                        })
                    }

                }
                override fun negativeBtnClicked() {
                }
            })
            alert.binding.titleTxt.text = "회원 탈퇴"
            alert.binding.bodyTxt.text = "정말 탈퇴 하시겠습니까 ?\n탈퇴를 원하시면 '동의'라고 적어주세요"
            alert.binding.positiveBtn.setTextColor(ContextCompat.getColor(mContext, R.color.red))
            alert.binding.positiveBtn.setBackgroundResource(R.drawable.r5_red_stroke_1dp)
            
        }
    }

    override fun setValues() {
        setUserData()

        when (GlobalData.loginUser!!.provider) {
            "kakao" -> binding.socialLoginImg.setImageResource(R.drawable.kakao_login_icon)
            "facebook" -> binding.socialLoginImg.setImageResource(R.drawable.facebook_login_icon)
            "naver" -> binding.socialLoginImg.setImageResource(R.drawable.naver_icon)
            "google" -> binding.socialLoginImg.setImageResource(R.drawable.google_login_icon)
            else -> binding.socialLoginImg.visibility = View.GONE
        }
    }

    fun setUserData() {

        Glide.with(mContext)
            .load(GlobalData.loginUser!!.profileImg)
            .into(binding.profileImg)

        binding.nicknameTxt.text = GlobalData.loginUser!!.nickname
        binding.emailTxt.text = GlobalData.loginUser!!.email
        binding.pointTxt.text = GlobalData.loginUser!!.point.toString()
    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

                val dataUri = it.data?.data

//            URi => 이미지 뷰의 사진(Glide)
                Glide.with(mContext).load(dataUri).into(binding.profileImg)

//            Uri -> File 형태로 변환 -> 그 파일의 실제 경로를 얻어낼 필요가 있다
                val file = File(URIPathHelper().getPath(mContext, dataUri!!))

//            파일을 retrofit 에 첨부할 수 있는 => ReqeustBody => MultipartBody  형태로 변환
                val fileReqBody = RequestBody.create("image/*".toMediaType(), file)
                val body = MultipartBody.Part.createFormData("profile_image", "myFile.jpg", fileReqBody)

                apiList.putRequestUserImage(body).enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {
                            GlobalData.loginUser = response.body()!!.data.user
                            Glide.with(mContext).load(GlobalData.loginUser!!.profileImg)

                            Toast.makeText(mContext, "프로필 사진이 변경되었습니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }
                })


            }
        }

    fun startNaverDeleteToken(){
        NidOAuthLogin().callDeleteTokenApi(mContext, object : OAuthLoginCallback {
            override fun onSuccess() {
               //서버에서 토큰 삭제에 성공한 상태입니다.
            }
            override fun onFailure(httpStatus: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                Log.d("naver", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d("naver", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        })
    }
}