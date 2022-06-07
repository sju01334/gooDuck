package com.nepplus.gooduck

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.nepplus.gooduck.api.APIList
import com.nepplus.gooduck.api.ServerApi
import retrofit2.Retrofit

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mContext : Context
    lateinit var retrofit: Retrofit
    lateinit var apiList : APIList

    lateinit var titleTxt : TextView
    lateinit var addBtn : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this

//        supportActionBar?.let{
//            setCustomActionBar()
//        }

        retrofit = ServerApi.getRetrofit(mContext)
        apiList = retrofit.create(APIList::class.java)
    }

    abstract fun setupEvents()
    abstract  fun setValues()

//    fun setCustomActionBar () {
//        val defActionBar = supportActionBar!!
//        defActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
//        defActionBar.setCustomView(R.layout.my_custom_action_bar)
//
//        val toolbar = defActionBar.customView.parent as Toolbar
//        toolbar.setContentInsetsAbsolute(0,0)
//
//        titleTxt = defActionBar.customView.findViewById(R.id.titleTxt)
//        addBtn = defActionBar.customView.findViewById(R.id.addBtn)
//
//    }
}