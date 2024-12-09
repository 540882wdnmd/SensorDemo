package com.example.sensordemo

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.SensorManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.sensordemo.MainViewModel.Companion.CD
import com.example.sensordemo.databinding.ActivityMainBinding
import com.example.sensordemo.databinding.LayoutLoadingBinding
import com.example.sensordemo.util.cancelToast
import com.example.sensordemo.util.registerSensorListeners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.p1ay1s.base.extension.toast
import com.p1ay1s.vbclass.ViewBindingActivity
import com.p1ay1s.vbclass.ui.ViewBindingDialog

class LoadingDialog(context: Context) : ViewBindingDialog<LayoutLoadingBinding>(context)

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
            pauseStopBtn.setOnClickListener {
                startPauseTimer()
                pauseStopBtn.text = if (isRunning) "暂停" else "开始"
            }

            finishBtn.setOnClickListener {
                if (isRunning) {
                    cancelToast()
                    "请先暂停".toast()
                    return@setOnClickListener
                }

                requireID {
                    dialog.show()

                    postJsonData(id) { isSuccess, msg ->
                        dialog.hide()
                        if (isSuccess) {
                            resetTimer()
                            cleanDataList()

                            MaterialAlertDialogBuilder(this@MainActivity)
                                .setTitle("上传的 json 预览")
                                .setMessage(msg)
                                .setCancelable(true)
                                .setPositiveButton("确认") { _, _ ->
                                }.create().show()
                        } else {
                            cancelToast()
                            "提交失败: $msg".toast()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(mainViewModel.listener)
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
                    cancelToast()
                    "学号不可留空, 请重新提交".toast()
                } else {
                    id = editText.text.toString()
                    callback()
                }
                isShowing = false
            }
            .setNegativeButton("取消") { _, _ ->
                cancelToast()
                "已取消提交".toast()
                isShowing = false
            }
            .setOnCancelListener {
                cancelToast()
                "已取消提交".toast()
                isShowing = false
            }.create().show()
    }
}