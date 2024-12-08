package com.example.sensordemo

import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.sensordemo.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.p1ay1s.base.extension.toast
import com.p1ay1s.vbclass.ViewBindingActivity

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    private var id: String? = null
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
                pauseStopBtn.text = "Start"
                if (requireID()) {
                    mainViewModel.resetTimer()
                    "已结束".toast()
                } else {
                    "结束失败".toast()
                }
            }
        }
    }

    private fun requireID(): Boolean {
        val editText = EditText(this).also {
            it.hint = "输入学号"
        }
        var result = false

        MaterialAlertDialogBuilder(this)
            .setTitle("输入学号")
            .setMessage("输入学号")
            .setCancelable(true)
            .setView(editText)
            .setNegativeButton("确认提交") { _, _ ->

                id = editText.toString()
                result = true
            }
            .setPositiveButton("取消提交") { _, _ ->

                result = false
            }.create().show()
        return result
    }
}