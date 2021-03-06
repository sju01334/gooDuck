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
import okhttp3.MediaType.Companion.toMediaType
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
                    val myIntent = Intent()
                    myIntent.action = Intent.ACTION_PICK
                    myIntent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
                    startForResult.launch(myIntent)
                }
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                }
            }
//            ????????? OK ??????
            TedPermission.create()
                .setPermissionListener(pl)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check()
        }

        binding.deleteImgBtn.setOnClickListener {
            pic = null
            binding.deleteImgBtn.visibility = View.GONE
            binding.addImageBtn.visibility = View.VISIBLE
            binding.image.visibility = View.GONE
            binding.imageTxt.visibility = View.GONE
        }


        binding.addTagBtn.setOnClickListener {
            val string = binding.tagEdt.text
            if (string.isNullOrEmpty()) {
                Toast.makeText(mContext, "????????? ??????????????????", Toast.LENGTH_SHORT).show()
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
//            ?????? ??????????????? ??????
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
                    Log.d("??????", output)
                }
                tagList = output.substring(0, output.length-1)
            }

            Log.d("?????????", tagList)

            val title = binding.reviewTitleEdt.text.toString()
            val content = binding.reviewContentEdt.text.toString()

            if (title.isEmpty()){
                Toast.makeText(mContext, "????????? ???????????????", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(content.isEmpty()){
                Toast.makeText(mContext, "????????? ???????????????", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(tagList == ""){
                Toast.makeText(mContext, "????????? 1????????? ???????????????", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val id_multi = MultipartBody.Part.createFormData("product_id", selectedProduct.id.toString())
            val title_multi = MultipartBody.Part.createFormData("title", title)
            val content_multi = MultipartBody.Part.createFormData("content", content)
            val rating_multi = MultipartBody.Part.createFormData("score", binding.ratingBar.rating.toString())
            val tags_multi = MultipartBody.Part.createFormData("tag_list", tagList)


            apiList.postRequestAddReview(
                id_multi,
                title_multi,
                content_multi,
                rating_multi,
                tags_multi,
                pic
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
                            Log.d("???????????? ??????_error",message)
                        }
                    }

                }
                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    Log.d("????????????", t.toString())
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
        subTitleTxt.text = "?????? ??????"


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
                    mProductList.clear()
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


//            ?????? ????????? ????????????? ???????????????
//            ?????? : ?????? ????????? profileImg ??? ?????? ?????????(???????????? X)

//            data? => ?????? ????????? ????????? intent
//            data?.data => ????????? ????????? ???????????? ?????? ??????(URI)
                val dataUri = it.data?.data

//            URi => ????????? ?????? ??????(Glide)
                Glide.with(mContext).load(dataUri).into(binding.image)
                binding.addImageBtn.visibility = View.GONE
                binding.imageTxt.visibility = View.VISIBLE
                binding.deleteImgBtn.visibility = View.VISIBLE
                binding.image.visibility = View.VISIBLE

//            API ????????? ????????? ?????? => PUT  ????????? + ("/user/image")
//            ????????? ?????? ?????? => Multipart ????????? ????????? ?????? ??????
//            Uri -> File ????????? ?????? -> ??? ????????? ?????? ????????? ????????? ????????? ??????
                val file = File(URIPathHelper().getPath(mContext, dataUri!!))

//            ????????? retrofit ??? ????????? ??? ?????? => ReqeustBody => MultipartBody  ????????? ??????
                val fileReqBody = RequestBody.create("image/*".toMediaType(), file)
                pic = MultipartBody.Part.createFormData("thumbnail_img", "myFile.jpg", fileReqBody)

            }
        }

}