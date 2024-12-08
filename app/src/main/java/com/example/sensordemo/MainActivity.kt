package com.example.sensordemo

import androidx.lifecycle.ViewModelProvider
import com.example.sensordemo.databinding.ActivityMainBinding
import com.p1ay1s.base.extension.toast
import com.p1ay1s.vbclass.ViewBindingActivity

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    private val mainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun ActivityMainBinding.initBinding() {
        viewModel = mainViewModel
        lifecycleOwner = this@MainActivity // 双向绑定

        pauseStopBtn.setOnClickListener {
            mainViewModel.startPauseTimer()
            if (mainViewModel.isRunning) {
                pauseStopBtn.text = "Pause"
                "开始计时".toast()
            } else {
                pauseStopBtn.text = "Start"
                "暂停计时".toast()
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