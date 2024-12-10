package com.example.sensordemo

import android.annotation.SuppressLint
import android.hardware.SensorManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.sensordemo.MainViewModel.Companion.CD
import com.example.sensordemo.databinding.ActivityMainBinding
import com.example.sensordemo.ui.LoadingDialog
import com.example.sensordemo.util.registerSensorListeners
import com.example.sensordemo.util.toast2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.p1ay1s.vbclass.ViewBindingActivity


@SuppressLint("SetTextI18n")
class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    private lateinit var sensorManager: SensorManager
    private var id: String = "未设置"
    private val mainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    private lateinit var dialog: LoadingDialog
    private var isShowing = false

    override fun ActivityMainBinding.initBinding() {
        enableEdgeToEdge()
        dialog = LoadingDialog(this@MainActivity)

        viewModel = mainViewModel
        lifecycleOwner = this@MainActivity // 双向绑定

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        cdTv.text = "当前记录间隔: 每${CD}毫秒"
        sensorManager.registerSensorListeners(mainViewModel.listener)

        mainViewModel.apply {
            startBtn.setOnClickListener {
                startPauseTimer()
                startBtn.text = if (isRunning) "暂停" else "开始"
            }

            finishBtn.setOnClickListener {
                if (isRunning) {
                    "请先暂停".toast2()
                    return@setOnClickListener
                }

                requireID {
                    dialog.show() // 显示 process bar
                    postJsonData(id) { isSuccess, msg ->
                        dialog.hide() // 隐藏 process bar
                        if (isSuccess) {
                            resetTimer()
                            cleanDataList()
                            showSimpleDialog("以下数据已上传", msg)
                        } else {
                            "提交失败: $msg".toast2()
                        }
                    }
                }
            }

            viewBtn.setOnClickListener {
                if (isRunning) {
                    "请先暂停".toast2()
                    return@setOnClickListener
                }

                val postData = getPostData(id)
                val msg =
                    if (postData.data.isNullOrEmpty()) "未收集到数据" else Gson().toJson(postData)
                showSimpleDialog("将要上传以下数据", msg)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(mainViewModel.listener)
    }

    private fun showSimpleDialog(title: String = "", msg: String = "") {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(msg)
            .setCancelable(true)
            .setPositiveButton("确认") { _, _ ->
            }.create()
            .show()
    }

    /**
     * 仅满足上传条件才回调
     */
    private fun requireID(callback: () -> Unit) {
        if (isShowing) return
        isShowing = true

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
                    "学号不可留空, 请重新提交".toast2()
                } else {
                    id = editText.text.toString()
                    callback()
                }
                isShowing = false
            }
            .setNegativeButton("取消") { _, _ ->
                "已取消提交".toast2()
                isShowing = false
            }
            .setOnCancelListener {
                "已取消提交".toast2()
                isShowing = false
            }.create().show()
    }
}