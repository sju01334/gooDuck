package com.nepplus.gooduck.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R

class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}