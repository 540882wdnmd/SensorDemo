package com.topviewclub.running

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.hardware.SensorManager
import android.os.Build
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.topviewclub.running.databinding.ActivityMainBinding
import com.topviewclub.running.ui.LoadingDialog
import com.topviewclub.running.util.parseToPrettyJson
import com.topviewclub.running.util.registerSensorListeners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zephyr.base.extension.toast
import com.zephyr.vbclass.ViewBindingActivity

const val BASE =
    "http://10.21.32.252:7172/" // 这个是后台工位上的服务器, 连着工作室的 wifi 就可以访问到, 现在后台没写状态码所以会超时, 实际上他那边是可以收到的

@SuppressLint("SetTextI18n")
class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    private lateinit var sensorManager: SensorManager
    private var id: String = "未设置"
    private val mainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    private lateinit var dialog: LoadingDialog
    private var isShowing = false

    @RequiresApi(Build.VERSION_CODES.S)
    override fun ActivityMainBinding.initBinding() {
        enableEdgeToEdge()

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.height.toFloat()
            )
            slideUp.run {
                interpolator = AnticipateInterpolator()
                duration = 600L
                doOnEnd { splashScreenView.remove() }
                start()
            }
        }

        dialog = LoadingDialog(this@MainActivity)

        viewModel = mainViewModel
        lifecycleOwner = this@MainActivity // 双向绑定

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.registerSensorListeners(mainViewModel.listener)

        mainViewModel.apply {
            cdTv.setOnClickListener {
                if (isRunning) {
                    "请先暂停".toast()
                    return@setOnClickListener
                }

                showInputDialog("设置一个新的值", "", "",
                    { str ->
                        try {
                            val num = str.toString().toLong()
                            if (num in 1L..10000L)  // 限定在合理的范围内
                                CD = num
                            else
                                "必须设置在 1~10000 之间".toast()
                        } catch (_: Exception) {
                            "必须设置为一个长整型数".toast()
                        }
                    })
            }

            startBtn.setOnClickListener {
                startPauseTimer()
                startBtn.text = if (isRunning) "暂停" else "开始"
            }

            finishBtn.setOnClickListener {
                if (isRunning) {
                    "请先暂停".toast()
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
                            "提交失败: $msg".toast()
                        }
                    }
                }
            }

            viewBtn.setOnClickListener {
                if (isRunning) {
                    "请先暂停".toast()
                    return@setOnClickListener
                }
                val toId = try {
                    id.toLong()
                } catch (_: Exception) {
                    -1
                }
                val postData = getPostData(toId)
                if (postData.data.isEmpty()) {
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
                    "学号不可留空, 请重新提交".toast()
                } else {
                    id = str.toString()
                    callback()
                }
            }, {
                "已取消提交".toast()
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