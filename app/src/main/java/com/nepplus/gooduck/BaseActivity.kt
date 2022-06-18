package com.nepplus.gooduck

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.nepplus.gooduck.api.APIList
import com.nepplus.gooduck.api.ServerApi
import com.nepplus.gooduck.ui.market.CartActivity
import retrofit2.Retrofit


abstract class BaseActivity : AppCompatActivity() {

    lateinit var mContext : Context
    lateinit var retrofit: Retrofit
    lateinit var apiList : APIList

    lateinit var titleTxt : TextView
    lateinit var subTitleTxt : TextView

    lateinit var bagBtn : ImageView
    lateinit var sideBarBtn : ImageView
    lateinit var backBtn : ImageView

    lateinit var background : FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this

        supportActionBar?.let{
            setCustomActionBar()
        }

        retrofit = ServerApi.getRetrofit(mContext)
        apiList = retrofit.create(APIList::class.java)
    }

    abstract fun setupEvents()
    abstract  fun setValues()

    open fun setCustomActionBar () {
        val defActionBar = supportActionBar!!
        defActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        defActionBar.setCustomView(R.layout.custom_action_bar)

        val toolbar = defActionBar.customView.parent as Toolbar
        toolbar.setContentInsetsAbsolute(0,0)

        titleTxt = defActionBar.customView.findViewById(R.id.titleTxt)
        subTitleTxt = defActionBar.customView.findViewById(R.id.subTitleTxt)
        bagBtn = defActionBar.customView.findViewById(R.id.bagBtn)
        sideBarBtn = defActionBar.customView.findViewById(R.id.sideBarBtn)
        backBtn = defActionBar.customView.findViewById(R.id.backBtn)
        background = defActionBar.customView.findViewById(R.id.background)

        bagBtn.setOnClickListener {
            val myInent = Intent(mContext, CartActivity::class.java)
            startActivity(myInent)
        }

        backBtn.setOnClickListener {
            finish()
        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view: View? = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x: Float = ev.rawX + view.getLeft() - scrcoords[0]
            val y: Float = ev.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) (this.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager).hideSoftInputFromWindow(
                this.window.decorView.applicationWindowToken, 0
            )
        }
        return super.dispatchTouchEvent(ev)
    }
}