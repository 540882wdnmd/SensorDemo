package com.example.sensordemo

import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.sensordemo.databinding.ActivityMainBinding
import com.p1ay1s.base.extension.TAG
import com.p1ay1s.base.extension.toast
import com.p1ay1s.vbclass.ViewBindingActivity

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    private val mainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun ActivityMainBinding.initBinding() {
        mainViewModel.timeString.observe(this@MainActivity) { time ->
            Log.d(TAG, time)
            timeTv.text = time
        }

        pauseStopBtn.setOnClickListener {
            mainViewModel.startPauseTimer()
            if (mainViewModel.isRunning) {
                pauseStopBtn.text = "Pause"
                "暂停计时".toast()
            } else {
                pauseStopBtn.text = "Start"
                "开始计时".toast()
            }
        }

        finishBtn.setOnClickListener {
            if (mainViewModel.isRunning) {
                "请先暂停".toast()
            } else {
                mainViewModel.resetTimer()
                "已结束".toast()
                pauseStopBtn.text = "Start"
            }
        }
    }
}