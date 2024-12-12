package com.example.sensordemo

import android.annotation.SuppressLint
import android.hardware.SensorManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.sensordemo.databinding.ActivityMainBinding
import com.example.sensordemo.ui.LoadingDialog
import com.example.sensordemo.util.parseToPrettyJson
import com.example.sensordemo.util.registerSensorListeners
import com.example.sensordemo.util.toast2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.p1ay1s.vbclass.ViewBindingActivity

//const val BASE = "http://10.21.32.252:7172/"
const val BASE = "https://c45402308c4a71.lhr.life//"

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

        sensorManager.registerSensorListeners(mainViewModel.listener)

        mainViewModel.apply {
            cdTv.setOnClickListener {
                if (isRunning) {
                    "请先暂停".toast2()
                    return@setOnClickListener
                }

                showInputDialog("设置一个新的值", "", "",
                    { str ->
                        try {
                            val num = str.toString().toLong()
                            if (num in 1L..10000L)  // 限定在合理的范围内
                                CD = num
                            else
                                "必须设置在 1~10000 之间".toast2()
                        } catch (_: Exception) {
                            "必须设置为一个长整型数".toast2()
                        }
                    })
            }

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
                    val toId = try {
                        id.toLong()
                    } catch (_: Exception) {
                        -1
                    }
                    postJsonData(toId) { isSuccess, msg ->
                        if (isSuccess) { // 网络请求成功
                            resetTimer()
                            cleanDataList()
                            dialog.hide() // 隐藏 process bar
                            showSimpleDialog("以下数据已上传", msg)
                        } else {
                            dialog.hide() // 隐藏 process bar
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
                val toId = try {
                    id.toLong()
                } catch (_: Exception) {
                    -1
                }
                val postData = getPostData(toId)
                if (postData.data.isNullOrEmpty()) {
                    showSimpleDialog("还未收集到数据")
                } else {
                    showJsonDialog("将要上传以下数据", postData)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(mainViewModel.listener)
    }

    /**
     * 加载对象为格式化的 json 并用 dialog 展示
     */
    private fun showJsonDialog(title: String, obj: Any?) {
        dialog.show()
        lifecycleScope.parseToPrettyJson(obj) {
            dialog.hide()
            showSimpleDialog(title, it)
        }
    }


    /**
     * 仅满足上传条件才回调
     */
    private fun requireID(callback: () -> Unit) {
        showInputDialog("数据提交", "输入你的学号", "请输入学号",
            { str ->
                if (str.isNullOrEmpty()) {
                    "学号不可留空, 请重新提交".toast2()
                } else {
                    id = str.toString()
                    callback()
                }
            }, {
                "已取消提交".toast2()
            })
    }

    private fun showInputDialog(
        title: String = "",
        msg: String = "",
        hint: String = "",
        onConfirmed: ((CharSequence?) -> Unit) = {},
        onCancel: (() -> Unit) = {}
    ) {
        if (isShowing) return
        isShowing = true

        val editText = EditText(this).also {
            it.hint = hint
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(msg)
            .setCancelable(true)
            .setView(editText)
            .setPositiveButton("确认") { _, _ ->
                isShowing = false
                onConfirmed(editText.text)
            }
            .setNegativeButton("取消") { _, _ ->
                isShowing = false
                onCancel()
            }
            .setOnCancelListener {
                isShowing = false
                onCancel()
            }.create().show()
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
}