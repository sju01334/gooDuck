package com.nepplus.gooduck.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.R.*
import com.nepplus.gooduck.adapters.MainViewPagerAdapter
import com.nepplus.gooduck.databinding.ActivityMainBinding
import com.nepplus.gooduck.dialog.CustomAlertDialog
import com.nepplus.gooduck.ui.market.CartActivity
import com.nepplus.gooduck.ui.setting.MyReviewListActivity
import com.nepplus.gooduck.ui.setting.PaymentListActivity
import com.nepplus.gooduck.ui.setting.PointListActivity
import com.nepplus.gooduck.utils.ContextUtil
import com.nepplus.gooduck.utils.GlobalData


class MainActivity : BaseActivity(){

    lateinit var binding : ActivityMainBinding

    lateinit var mPagerAdapter : MainViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {


        setSupportActionBar(binding.mainToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(drawable.three_line)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout,binding.mainToolbar, string.navigation_drawer_open, string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

//        네비 헤더 메뉴 클릭 리스너
        val header = binding.navView.getHeaderView(0)
        val logout = header.findViewById<Button>(R.id.logoutBtn)
        val myPage = header.findViewById<Button>(R.id.myPageBtn)
        val txt = header.findViewById<TextView>(id.Txt)
        val manageBuyLayout = header.findViewById<LinearLayout>(id.manageBuyLayout)
        val manageReviewLayout = header.findViewById<LinearLayout>(id.manageReviewLayout)
        val managePointLayout = header.findViewById<LinearLayout>(id.managePointLayout)

        txt.text = "안녕하세요. ${GlobalData.loginUser!!.nickname}님, 환영합니다!"

        logout.setOnClickListener {
            //        로그아웃
            val alert = CustomAlertDialog(mContext)
            alert.myDialog(object : CustomAlertDialog.ButtonClickListener{
                override fun positiveBtnClicked() {
                    alert.dialog.dismiss()
                    ContextUtil.clear(mContext)
                    GlobalData.loginUser = null
                    val myIntent = Intent(mContext, LoginActivity::class.java)
                    myIntent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(myIntent)
                }
                override fun negativeBtnClicked() {
                }
            })

            alert.binding.titleTxt.text = "로그 아웃"
            alert.binding.bodyTxt.text = "정말 로그아웃 하시겠습니까 ?"
            alert.binding.contentEdt1.visibility = View.GONE
            alert.binding.positiveBtn.setBackgroundResource(R.drawable.r5_yellow_stroke_1dp)
        }

        myPage.setOnClickListener {  }

        manageReviewLayout.setOnClickListener{
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val myIntent = Intent(mContext, MyReviewListActivity::class.java)
            startActivity(myIntent)
        }

        manageBuyLayout.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val myIntent = Intent(mContext, PaymentListActivity::class.java)
            startActivity(myIntent)
        }

        managePointLayout.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val myIntent = Intent(mContext, PointListActivity::class.java)
            startActivity(myIntent)
        }




        binding.navView.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    id.nav_home -> {

                        Toast.makeText(mContext, "1111", Toast.LENGTH_SHORT).show()
                        Log.d("눌렸니", "yes")
                        val myIntent = Intent(mContext, CartActivity::class.java)
                        startActivity(myIntent)

                    }
                    id.nav_gallery -> {}
                    else  -> {}
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                return false
            }
        })





    }

    override fun setValues() {


        mPagerAdapter = MainViewPagerAdapter(this)
        binding.mainViewPager.adapter = mPagerAdapter

        binding.mainViewPager.currentItem = 1
        binding.mainViewPager.isUserInputEnabled = false
        binding.bottomNav.selectedItemId = id.market

        binding.mainViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNav.menu.getItem(position).isChecked = true
                }
            })

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                id.review -> binding.mainViewPager.currentItem = 0
                id.market -> binding.mainViewPager.currentItem = 1
                id.settings -> binding.mainViewPager.currentItem = 2
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            id.topNavCart -> {
                val myInent = Intent(mContext, CartActivity::class.java)
                startActivity(myInent)
            }
            else ->{
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }

    }

}


