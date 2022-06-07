package com.nepplus.gooduck.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ActivitySignupBinding

class SignupActivity : BaseActivity() {

    lateinit var binding : ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup)
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}