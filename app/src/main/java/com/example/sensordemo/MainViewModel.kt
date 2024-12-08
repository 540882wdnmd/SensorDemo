package com.example.sensordemo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.p1ay1s.base.extension.TAG
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class MainViewModel : ViewModel() {
    var isRunning = false
    private var elapsedTime = 0L
    private val _timeString = MutableLiveData<String>()
    val timeString: LiveData<String>
        get() = _timeString

    private var timerJob: Job? = null

    fun startPauseTimer() {
        if (isRunning) {
            isRunning = false
            timerJob?.cancel()
        } else {
            isRunning = true
            timerJob = CoroutineScope(Dispatchers.Main).launch {
                try {
                    while (isRunning) {
                        elapsedTime += 1000
                        val timeText = elapsedTime.toTimeFormat()
                        _timeString.value = timeText
                        Log.d(TAG, "计时成功$elapsedTime")
                        delay(1000)
                    }
                } catch (e: CancellationException) {
                    Log.d(TAG, e.toString())
                }
            }
        }
    }

    fun resetTimer() {
        isRunning = false
        elapsedTime = 0
        _timeString.value = "00:00:00"
        timerJob?.cancel()
    }

    private fun Long.toTimeFormat(): String {
        val hours = TimeUnit.MILLISECONDS.toHours(this)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(this) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60
        Log.d(TAG, "toTimeFormat:$hours:$minutes:$seconds")
        return String.format(
            Locale.US,
            "%02d:%02d:%02d",
            hours,
            minutes,
            seconds
        )
    }
}