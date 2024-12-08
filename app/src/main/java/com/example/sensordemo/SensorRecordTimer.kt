package com.example.sensordemo

/**
 * 用于约束记录的频次
 * 具体的记录逻辑可通过 callback 设置
 * @param cd 规定的冷却时间(多久允许记录一次)
 */
class SensorRecordTimer(private val cd: Long) {
    private var lastRecordTime = System.currentTimeMillis()
    var callback: (() -> Unit)? = null

    /**
     * 达到冷却时间或选择强制记录时调用 callback
     */
    fun record(force: Boolean = false) {
        val now = System.currentTimeMillis()
        if (force || now - lastRecordTime >= cd) {
            lastRecordTime = System.currentTimeMillis() // 更新最后的记录时间戳
            callback?.invoke() // 调用回调函数
        }
    }
}