package com.example.sensordemo.util

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.EditText
import com.example.sensordemo.TestActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

const val ALL_SENSORS = "All_Sensors"
const val MOTION_SENSORS = "Motion_Sensors"
const val POSITION_SENSORS = "Position_Sensors"
const val ENVIRONMENT_SENSORS = "Environment_Sensors"
const val SUPPORT = "Support"


fun registerSensor(sensorManager:SensorManager?,sensorEventListener:SensorEventListener){
    sensorManager?.run {
        // 加速度传感器
        // 加速度传感器=重力传感器+线性加速度传感器
        registerListener(
            sensorEventListener,
            getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_UI
        )

        // 有关偏差补偿的三轴加速度传感器
        registerListener(
            sensorEventListener,
            getDefaultSensor(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED),
            SensorManager.SENSOR_DELAY_UI
        )

        // 重力传感器
        registerListener(
            sensorEventListener,
            getDefaultSensor(Sensor.TYPE_GRAVITY),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // 陀螺仪传感器 有关旋转速度的传感器
        registerListener(
            sensorEventListener,
            getDefaultSensor(Sensor.TYPE_GYROSCOPE),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // 有关漂移补偿三轴的旋转加速器
        registerListener(
            sensorEventListener,
            getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // 线性加速度传感器(即不包含重力的加速器)
        registerListener(
            sensorEventListener,
            getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // 旋转矢量传感器
        registerListener(
            sensorEventListener,
            getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        /**
         * 位置传感器(Position Sensors)
         */

        // 游戏旋转矢量传感器
        registerListener(
            sensorEventListener,
            getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // 地磁旋转矢量传感器
        registerListener(
            sensorEventListener,
            getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // 三轴的地磁场强度传感器
        registerListener(
            sensorEventListener,
            getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // 有关硬铁校准功能的三轴的地磁场强度传感器
        registerListener(
            sensorEventListener,
            getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // 有关角度的三轴传感器(此方法已在API15弃用)
        registerListener(
            sensorEventListener,
            getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // 与物体距离
        registerListener(
            sensorEventListener,
            getDefaultSensor(Sensor.TYPE_PROXIMITY),
            SensorManager.SENSOR_DELAY_NORMAL
        )

    }
}

class SensorEventListenerImp : SensorEventListener {
    override fun onSensorChanged(event: SensorEvent?) {
        val values = event?.values ?: return
        val sensorType = event.sensor?.type ?: return
        when (sensorType) {
            Sensor.TYPE_ACCELEROMETER -> {
                Log.d(MOTION_SENSORS, "加速度传感器")

            }

            Sensor.TYPE_ACCELEROMETER_UNCALIBRATED -> {
                Log.d(MOTION_SENSORS, "加速度传感器(有关偏差补偿)")

            }

            Sensor.TYPE_GRAVITY -> {
                Log.d(MOTION_SENSORS, "重力加速度传感器")

            }

            Sensor.TYPE_GYROSCOPE -> {
                Log.d(MOTION_SENSORS, "旋转速率传感器")

            }

            Sensor.TYPE_GYROSCOPE_UNCALIBRATED -> {
                Log.d(MOTION_SENSORS, "旋转速率传感器(有关漂移补偿)")

            }

            Sensor.TYPE_LINEAR_ACCELERATION -> {
                Log.d(MOTION_SENSORS, "线性加速度传感器(不包含重力)")

            }

            Sensor.TYPE_ROTATION_VECTOR -> {
                Log.d(MOTION_SENSORS, "三轴旋转矢量分量")

            }

            Sensor.TYPE_GAME_ROTATION_VECTOR -> {
                Log.d(POSITION_SENSORS, "游戏旋转矢量传感器")

            }

            Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR -> {
                Log.d(POSITION_SENSORS, "地磁旋转矢量传感器")

            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                Log.d(POSITION_SENSORS, "地磁场强度传感器")

            }

            Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED -> {
                Log.d(POSITION_SENSORS, "地磁场强度传感器(有关硬铁校准)")

            }

            Sensor.TYPE_ORIENTATION -> {
                Log.d(POSITION_SENSORS, "三轴角度")

            }

            Sensor.TYPE_PROXIMITY -> {
                Log.d(POSITION_SENSORS, "与物体距离 ")

            }

        }


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}

