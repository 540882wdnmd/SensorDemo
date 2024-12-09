package com.example.sensordemo

import android.annotation.SuppressLint
import android.hardware.SensorManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.sensordemo.MainViewModel.Companion.CD
import com.example.sensordemo.databinding.ActivityMainBinding
import com.example.sensordemo.util.registerSensorListeners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.p1ay1s.base.extension.toast
import com.p1ay1s.vbclass.ViewBindingActivity

@SuppressLint("SetTextI18n")
class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    private lateinit var sensorManager: SensorManager
    private var id: String = "未设置"
    private val mainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun ActivityMainBinding.initBinding() {
        enableEdgeToEdge()

        viewModel = mainViewModel
        lifecycleOwner = this@MainActivity // 双向绑定

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        cdTv.text = "当前记录间隔: 每${CD}毫秒"

        pauseStopBtn.setOnClickListener {
            mainViewModel.startPauseTimer()
            if (mainViewModel.isRunning) {
                pauseStopBtn.text = "暂停"
                "开始计时".toast()
            } else {
                pauseStopBtn.text = "开始"
                "暂停计时".toast()
            }
        }

        finishBtn.setOnClickListener {
            if (mainViewModel.isRunning) {
                "请先暂停".toast()
            } else {
                requireID {
                    if (it) {
                        mainViewModel.postJsonData(id) { isSuccess, msg ->
                            if (isSuccess) {
                                "提交成功".toast()
                                mainViewModel.run {
                                    resetTimer()
                                    cleanDataList()
                                }
                            } else {
                                "提交失败, 原因: $msg".toast()
                            }
                        }
                    }
                }
            }
        }

        sensorManager.registerSensorListeners(mainViewModel.listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(mainViewModel.listener)
    }

    private fun requireID(callback: (Boolean) -> Unit) {
        val editText = EditText(this).also {
            it.hint = "请输入学号"
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("数据提交")
            .setMessage("输入你的学号")
            .setCancelable(true)
            .setView(editText)
            .setPositiveButton("确认") { _, _ ->
                val str = editText.text
                if (str.isNullOrEmpty()) {
                    callback(false)
                    "学号不可留空, 请重新提交".toast()
                } else {
                    id = editText.text.toString()
                    callback(true)
                }
            }
            .setNegativeButton("取消") { _, _ ->
                callback(false)
                "已取消提交".toast()
            }.create().show()
    }
}