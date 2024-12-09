package com.example.sensordemo.util

import android.util.Log

/**
 * 用于约束记录的频次
 * 具体的记录逻辑可通过 callback 设置
 * @param cd 规定的冷却时间(多久允许记录一次)
 */
class SensorRecordTimer(private val cd: Long) {
    private var lastRecordTime = 0L
    private var lastRecordedValues: FloatArray? = null
    val values: FloatArray?
        get() = lastRecordedValues
    private var callback: ((values: FloatArray) -> Unit)? = null


    /**
     * 达到冷却时间或选择强制记录时调用 callback
     * @param values 传入 event?.values 即可
     */
    fun record(values: FloatArray?, force: Boolean = false) {
        val now = System.currentTimeMillis()
        if (force || now - lastRecordTime >= cd) {
            lastRecordTime = System.currentTimeMillis() // 更新最后的记录时间戳
            if (values == null) return

            try {
                callback?.invoke(values) // 调用回调函数
                lastRecordedValues = values
            } catch (e: Exception) {
                Log.e(this::class.simpleName, e.stackTrace.toString())
            }
        }
    }

    fun setTimerCallback(callback: ((values: FloatArray) -> Unit)?) {
        this.callback = callback
    }
}