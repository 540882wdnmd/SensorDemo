package com.example.sensordemo

import android.hardware.SensorManager
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.sensordemo.bean.PostData
import com.example.sensordemo.databinding.ActivityMainBinding
import com.example.sensordemo.util.registerSensorListeners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.p1ay1s.base.extension.toast
import com.p1ay1s.vbclass.ViewBindingActivity

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    private lateinit var sensorManager: SensorManager
    private var id: String = ""
    private val mainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun ActivityMainBinding.initBinding() {
        viewModel = mainViewModel
        lifecycleOwner = this@MainActivity // 双向绑定

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

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
                requireID {
                    if (it) {
                        id.toast()
                        mainViewModel.run {
                            resetTimer()
                            postJsonData(
                                PostData(
                                    id,
                                    mainViewModel.timeString.value.toString(),
                                    mainViewModel.sensorDataList,
                                    true
                                )
                            )
                        }
                        "已结束".toast()
                    } else {
                        "结束失败".toast()
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
            it.hint = "输入学号 - hint"
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("输入学号 - title")
            .setMessage("输入学号 - msg")
            .setCancelable(true)
            .setView(editText)
            .setPositiveButton("确认提交") { _, _ ->
                id = editText.text.toString()
                callback(true)
            }
            .setNegativeButton("取消提交") { _, _ ->
                callback(false)
            }.create().show()
    }
}