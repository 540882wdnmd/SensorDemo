package com.example.sensordemo

import android.content.Intent
import com.example.sensordemo.databinding.ActivityMainBinding
import com.p1ay1s.vbclass.ViewBindingActivity

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {
    override fun ActivityMainBinding.initBinding() {
        startActivity(Intent(this@MainActivity, TestActivity::class.java))
    }
}