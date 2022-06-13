package com.nepplus.gooduck.ui.review

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ActivityReviewAddBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Product
import com.nepplus.gooduck.utils.URIPathHelper
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ReviewAddActivity : BaseActivity() {

    lateinit var binding : ActivityReviewAddBinding
    lateinit var selectedProduct : Product

    var mProductList = ArrayList<Product>()
    var mProductStringList = ArrayList<String>()
    var mCategoryStringList = ArrayList<String>()

    var pic : MultipartBody.Part? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_add)

        getSmallCategory()
        setupEvents()
        setValues()


    }

    override fun setupEvents() {

        binding.addImageBtn.setOnClickListener {
            val pl = object : PermissionListener {
                override fun onPermissionGranted() {
//            갤러리로 사진 가지러 이동(추가작업) => Intent(4)
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


        binding.addTagBtn.setOnClickListener {
            val string = binding.tagEdt.text
            if (string.isNullOrEmpty()) {
                Toast.makeText(mContext, "태그를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                binding.chipGroup.addView(Chip(this).apply {
                    text = string
                    binding.tagEdt.text.clear()
                    isCloseIconVisible = true
                    setOnCloseIconClickListener { binding.chipGroup.removeView(this) }
                })

            }
        }

        binding.addReview.setOnClickListener {
//            태그 스트링으로 변경
            var tagList = ""
            if(binding.chipGroup.childCount !=0){
                val chipList = ArrayList<String>()
                for (i: Int in 1..binding.chipGroup.childCount) {
                    val chip: Chip = binding.chipGroup.getChildAt(i - 1) as Chip
                    chipList.add(chip.text.toString())
                }

                var output = ""
                for (i in chipList) {
                    output += "$i,"
                    Log.d("뭐야", output)
                }
                tagList = output.substring(0, output.length-1)
            }

            Log.d("태그들", tagList)

            val title = binding.reviewTitleEdt.text.toString()
            val content = binding.reviewContentEdt.text.toString()

            if (title.isEmpty()){
                Toast.makeText(mContext, "제목을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(content.isEmpty()){
                Toast.makeText(mContext, "내용을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(tagList == ""){
                Toast.makeText(mContext, "태그를 1개이상 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("아이디", selectedProduct.id.toString())
            Log.d("11", title)
            Log.d("22", content)
            Log.d("33", binding.ratingBar.rating.toString())
            Log.d("44", tagList)
            Log.d("55", pic.toString())


            apiList.postRequestAddReview(
                selectedProduct.id,
                title,
                content,
                binding.ratingBar.rating,
                tagList,
                pic!!
            ).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        Toast.makeText(mContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }else {
                        val errorBodyStr = response.errorBody()!!.string()
                        val jsonObj = JSONObject(errorBodyStr)
                        val code = jsonObj.getInt("code")
                        val message = jsonObj.getString("message")
                        if(code == 400){
                            Log.d("리뷰등록 실패_error",message)
                        }
                    }

                }
                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    Log.d("호출실패", t.toString())
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
        subTitleTxt.text = "리뷰 등록"
        background.setBackgroundColor(getColor(R.color.secondary))


    }

    fun getSmallCategory(){
        apiList.getRequestSmallCategory().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mCategoryStringList.clear()
                    mCategoryStringList.addAll(br.data.categories.map { it.name })

                    val categoryAdapter =ArrayAdapter(
                        mContext,
                        androidx.databinding.library.baseAdapters.R.layout.support_simple_spinner_dropdown_item,
                        mCategoryStringList)
                    binding.categorySpinner.adapter = categoryAdapter

                    binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long)
                        { getProduct(position+1) }
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }
                    }
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })
    }

    fun getProduct(cId : Int){
        apiList.getRequestProducts(cId).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mProductStringList.clear()
                    mProductStringList.addAll(br.data.products.map { it.name })
                    mProductList.addAll(br.data.products)

                    val adapter =ArrayAdapter(
                        mContext,
                        androidx.databinding.library.baseAdapters.R.layout.support_simple_spinner_dropdown_item,
                        mProductStringList)
                    binding.itemSpinner.adapter = adapter

                    binding.itemSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long
                        ) { selectedProduct = mProductList[position] }
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }
                    }
                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }
        })
    }


    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

                binding.image.visibility = View.VISIBLE
//            어떤 사진을 골랐는지? 파악해보자
//            임시 : 고른 사진을 profileImg 에 바로 적용만(서버전송 X)

//            data? => 이전 화면이 넘겨준 intent
//            data?.data => 선택한 사진이 들어있는 경로 정보(URI)
                val dataUri = it.data?.data

//            URi => 이미지 뷰의 사진(Glide)
                Glide.with(mContext).load(dataUri).into(binding.image)

//            API 서버에 사지을 전송 => PUT  메쏘드 + ("/user/image")
//            파일을 같이 첨부 => Multipart 형식의 데이터 첨부 활용
//            Uri -> File 형태로 변환 -> 그 파일의 실제 경로를 얻어낼 필요가 있다
                val file = File(URIPathHelper().getPath(mContext, dataUri!!))

//            파일을 retrofit 에 첨부할 수 있는 => ReqeustBody => MultipartBody  형태로 변환
                val fileReqBody = RequestBody.create(MediaType.get("image/*"), file)
                pic = MultipartBody.Part.createFormData("review_images", "myFile.jpg", fileReqBody)

            }
        }

}