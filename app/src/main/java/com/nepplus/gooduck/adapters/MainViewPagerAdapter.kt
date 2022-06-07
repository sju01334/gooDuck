package com.nepplus.gooduck.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nepplus.gooduck.fragments.MarketFragment
import com.nepplus.gooduck.fragments.ReviewFragment
import com.nepplus.gooduck.fragments.SettingFragment

class MainViewPagerAdapter(fa : FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ReviewFragment()
            1 -> MarketFragment()
            else -> SettingFragment()
        }
    }


}