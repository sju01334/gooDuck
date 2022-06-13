package com.nepplus.gooduck.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.MainViewPagerAdapter
import com.nepplus.gooduck.databinding.ActivityMainBinding
import com.nepplus.gooduck.ui.market.CartActivity


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding : ActivityMainBinding

    lateinit var mPagerAdapter : MainViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {



//        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        binding.drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
//
//        binding.mainToolbar.bagBtn.setOnClickListener {
//            val myInent = Intent(mContext, CartActivity::class.java)
//            startActivity(myInent)
//        }
//
//        binding.mainToolbar.backBtn.setOnClickListener {
//            finish()
//        }

        sideBarBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navView)
        }
        binding.navView.setNavigationItemSelectedListener(this)


    }

    override fun setValues() {


        mPagerAdapter = MainViewPagerAdapter(this)
        binding.mainViewPager.adapter = mPagerAdapter

        binding.mainViewPager.currentItem = 1
        binding.mainViewPager.isUserInputEnabled = false
        binding.bottomNav.selectedItemId = R.id.market

        binding.mainViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNav.menu.getItem(position).isChecked = true
                }
            })

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.review -> binding.mainViewPager.currentItem = 0
                R.id.market -> binding.mainViewPager.currentItem = 1
                R.id.settings -> binding.mainViewPager.currentItem = 2
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                Toast.makeText(mContext, "1111", Toast.LENGTH_SHORT).show()
                Log.d("눌렸니", "yes")
                val myIntent = Intent(mContext, CartActivity::class.java)
                startActivity(myIntent)

            }
            R.id.nav_gallery -> {}
            else  -> {}
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}


