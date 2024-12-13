package com.example.sensordemo.util

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import com.p1ay1s.base.appContext
import com.p1ay1s.base.extension.toast
import com.p1ay1s.base.log.logE
import com.p1ay1s.util.toPrettyJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val ALL_SENSORS = "All_Sensors"
const val MOTION_SENSORS = "Motion_Sensors"
const val POSITION_SENSORS = "Position_Sensors"
const val ENVIRONMENT_SENSORS = "Environment_Sensors"
const val SUPPORT = "Support"

fun Any?.toast2() {
    Toast(appContext).cancel()
    val str = this.toString()
    if (str.isNotBlank()) toast(str)
}

/**
 * 解析对象为格式化 json 字串, 然后在主线程回调
 *
 * 本来以为这个格式化久导致 dialog 延迟很久才出来, 结果发现可能是因为字串比较长渲染的比较久
 */
fun CoroutineScope.parseToPrettyJson(obj: Any?, callback: (String) -> Unit) {
    launch(Dispatchers.IO) {
        val json = obj.toPrettyJson()
        logE("JSON", json)
        withContext(Dispatchers.Main) {
            callback(json)
        }
    }
}

fun SensorManager.registerSensorListeners(listener: SensorEventListener?) {
    // 加速度传感器
    // 加速度传感器=重力传感器+线性加速度传感器
    registerListener(
        listener,
        getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        SensorManager.SENSOR_DELAY_UI
    )

    // 有关偏差补偿的三轴加速度传感器
    registerListener(
        listener,
        getDefaultSensor(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED),
        SensorManager.SENSOR_DELAY_UI
    )

    // 重力传感器
    registerListener(
        listener,
        getDefaultSensor(Sensor.TYPE_GRAVITY),
        SensorManager.SENSOR_DELAY_NORMAL
    )

    // 陀螺仪传感器 有关旋转速度的传感器
    registerListener(
        listener,
        getDefaultSensor(Sensor.TYPE_GYROSCOPE),
        SensorManager.SENSOR_DELAY_NORMAL
    )

    // 有关漂移补偿三轴的旋转加速器
    registerListener(
        listener,
        getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED),
        SensorManager.SENSOR_DELAY_NORMAL
    )

    // 线性加速度传感器(即不包含重力的加速器)
    registerListener(
        listener,
        getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
        SensorManager.SENSOR_DELAY_NORMAL
    )

    // 旋转矢量传感器
    registerListener(
        listener,
        getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
        SensorManager.SENSOR_DELAY_NORMAL
    )

    /**
     * 位置传感器(Position Sensors)
     */

    // 游戏旋转矢量传感器
    registerListener(
        listener,
        getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR),
        SensorManager.SENSOR_DELAY_NORMAL
    )

    // 地磁旋转矢量传感器
    registerListener(
        listener,
        getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR),
        SensorManager.SENSOR_DELAY_NORMAL
    )

    // 三轴的地磁场强度传感器
    registerListener(
        listener,
        getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
        SensorManager.SENSOR_DELAY_NORMAL
    )

    // 有关硬铁校准功能的三轴的地磁场强度传感器
    registerListener(
        listener,
        getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED),
        SensorManager.SENSOR_DELAY_NORMAL
    )

    // 有关角度的三轴传感器(此方法已在API15弃用)
    registerListener(
        listener,
        getDefaultSensor(Sensor.TYPE_ORIENTATION),
        SensorManager.SENSOR_DELAY_NORMAL
    )

    // 与物体距离
    registerListener(
        listener,
        getDefaultSensor(Sensor.TYPE_PROXIMITY),
        SensorManager.SENSOR_DELAY_NORMAL
    )
}

