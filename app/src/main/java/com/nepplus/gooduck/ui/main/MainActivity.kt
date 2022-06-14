package com.nepplus.gooduck.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.R.*
import com.nepplus.gooduck.adapters.MainViewPagerAdapter
import com.nepplus.gooduck.databinding.ActivityCardAddBinding.inflate
import com.nepplus.gooduck.databinding.ActivityMainBinding
import com.nepplus.gooduck.ui.market.CartActivity


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
                return true
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


