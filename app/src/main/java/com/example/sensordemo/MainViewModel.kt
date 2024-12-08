package com.example.sensordemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {
    companion object {
        const val REFRESH_CD = 10L // 刷新间隔
    }

    private var startTime: Long = 0
    private var elapsedTime: Long = 0

    private var timerJob: Job? = null

    private var _isRunning: Boolean = false
    val isRunning: Boolean
        get() = _isRunning

    private val _timeString = MutableLiveData("00:00:00.000")
    val timeString: LiveData<String>
        get() = _timeString

    private val mainModel: MainModel by lazy { MainModel() }

    fun postJsonData(json: String) {
        mainModel.postJsonData(json,
            { response ->
            }, // on success
            { code, msg -> } // on error. 状态码可空
        )
    }

    fun startPauseTimer() {
        if (isRunning)
            pauseTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        if (_isRunning) return

        _isRunning = true
        // 改用时间戳计算, 可以精确至毫秒
        startTime = System.currentTimeMillis() - elapsedTime // 减去 elapsedTime 以继续未结束的计时
        timerJob = viewModelScope.launch(Dispatchers.Main) {
            while (true) {
                elapsedTime = System.currentTimeMillis() - startTime
                _timeString.value = elapsedTime.toTimeFormat()
                delay(REFRESH_CD)
            }
        }
    }

    private fun pauseTimer() {
        if (!_isRunning) return

        _isRunning = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        if (_isRunning) return

        timerJob?.cancel()
        startTime = 0
        elapsedTime = 0
        _timeString.value = "00:00:00.000"
    }

    private fun Long.toTimeFormat(): String {
        val hours = TimeUnit.MILLISECONDS.toHours(this)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(this) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60
        val milliseconds = this % 1000

        return String.format(
            Locale.CHINA,
            "%02d:%02d:%02d.%03d",
            hours,
            minutes,
            seconds,
            milliseconds
        )
    }
}