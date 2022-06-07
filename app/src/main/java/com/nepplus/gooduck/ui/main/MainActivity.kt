package com.nepplus.gooduck.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.MainViewPagerAdapter
import com.nepplus.gooduck.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

    lateinit var mPagerAdapter : MainViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

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
}