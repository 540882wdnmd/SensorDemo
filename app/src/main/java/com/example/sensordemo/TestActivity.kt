package com.example.sensordemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.sensordemo.databinding.ActivityTestBinding
import com.example.sensordemo.util.registerSensorListeners
import com.p1ay1s.base.extension.toast
import com.p1ay1s.base.extension.withPermission
import com.p1ay1s.vbclass.ViewBindingActivity

@SuppressLint("SetTextI18n")
@RequiresApi(Build.VERSION_CODES.Q)
class TestActivity : ViewBindingActivity<ActivityTestBinding>() {
    companion object {
        const val ALL_SENSORS = "All_Sensors"
        const val MOTION_SENSORS = "Motion_Sensors"
        const val POSITION_SENSORS = "Position_Sensors"
        const val SUPPORT = "Support"

        const val CD = 500L
    }

    private var sensorManager: SensorManager? = null // 在类中创建SensorManager传感器管理器
    private var sensorEventListener: SensorEventListenerImpl? = null

    private lateinit var sb: StringBuilder

    private val timerAccelerometer = SensorRecordTimer(CD)
    private val timerAccelerometerUncalibrated = SensorRecordTimer(CD)
    private val timerGravity = SensorRecordTimer(CD)
    private val timerGyroscope = SensorRecordTimer(CD)
    private val timerGyroscopeUncalibrated = SensorRecordTimer(CD)
    private val timerLinearAcceleration = SensorRecordTimer(CD)
    private val timerRotationVector = SensorRecordTimer(CD)
    private val timerGameRotationVector = SensorRecordTimer(CD)
    private val timerGeomagneticRotationVector = SensorRecordTimer(CD)
    private val timerMagneticField = SensorRecordTimer(CD)
    private val timerMagneticFieldUncalibrated = SensorRecordTimer(CD)
    private val timerOrientation = SensorRecordTimer(CD)
    private val timerProximity = SensorRecordTimer(CD)

    override fun ActivityTestBinding.initBinding() {

        // 实例化SensorManager，获取传感器服务
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorEventListener = SensorEventListenerImpl()

        // 获取当前手机支持哪些传感器
        val list = sensorManager?.getSensorList(Sensor.TYPE_ALL)
        list?.let {
            for (i in it.indices) {
                Log.d(ALL_SENSORS, it[i].name)
            }
        }

        withPermission(Manifest.permission.ACTIVITY_RECOGNITION) {
            if (!it) {
                "请给予权限以进行运动检测".toast()
            }
        }

        sensorEventListener?.let { sensorManager?.registerSensorListeners(it) }
        initTimers()

        Log.d(
            SUPPORT,
            "是否支持StepCounter:" + packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
                .toString()
        )
        Log.d(
            SUPPORT,
            "是否支持StepDetector:" + packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)
                .toString()
        )
        Log.d(
            SUPPORT,
            "是否支持AMBIENT_TEMPERATURE:" + packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE)
                .toString()
        )
    }

    override fun onResume() {
        super.onResume()

        // 在onResume中向SensorManager注册传感器
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

            // 显著运动传感器(用来激活传感器的一个常量)
            registerListener(
                sensorEventListener,
                getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION),
                SensorManager.SENSOR_DELAY_NORMAL
            )

            // 步数记录传感器
            registerListener(
                sensorEventListener,
                getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_FASTEST
            )


            // 步数检测传感器(辅助计算的一个传感器)
            registerListener(
                sensorEventListener,
                getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                SensorManager.SENSOR_DELAY_FASTEST
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

            /**
             * 环境传感器
             */

            // 环境空气温度
            registerListener(
                sensorEventListener,
                getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_NORMAL
            )

            // 光照强度
            registerListener(
                sensorEventListener,
                getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL
            )

            // 环境气压强度
            registerListener(
                sensorEventListener,
                getDefaultSensor(Sensor.TYPE_PRESSURE),
                SensorManager.SENSOR_DELAY_NORMAL
            )

            // 环境相对湿度
            registerListener(
                sensorEventListener,
                getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY),
                SensorManager.SENSOR_DELAY_NORMAL
            )

//            // 设备温度(API15已弃用)
//            registerListener(
//                sensorEventListener,
//                getDefaultSensor(Sensor.TYPE_TEMPERATURE),
//                SensorManager.SENSOR_DELAY_NORMAL
//            )
        }
    }

    override fun onStop() {
        // 在super前调用，表示先清空子类，再清空父类
        sensorManager?.unregisterListener(sensorEventListener)
        sensorEventListener = null
        super.onStop()
    }

    private fun initTimers() = binding.run {
        timerAccelerometer.setTimerCallback { values ->
            Log.d(MOTION_SENSORS, "加速度传感器")
            sb = StringBuilder()
                .append("\n沿 x 轴的加速度：")
                .append(values[0])
                .append("\n沿 y 轴的加速度：")
                .append(values[1])
                .append("\n沿 z 轴的加速度：")
                .append(values[2])
            txt1.text = "加速度传感器(包含重力)：$sb"
        }
        timerAccelerometerUncalibrated.setTimerCallback { values ->
            Log.d(MOTION_SENSORS, "加速度传感器(有关偏差补偿)")
            sb = StringBuilder()
                .append("\n沿 x 轴的加速度,没有任何偏差补偿：")
                .append(values[0])
                .append("\n沿 y 轴的加速度,没有任何偏差补偿：")
                .append(values[1])
                .append("\n沿 z 轴的加速度,没有任何偏差补偿：")
                .append(values[2])
                .append("\n沿 x 轴的加速度,并带有估算的偏差补偿：")
                .append(values[3])
                .append("\n沿 y 轴的加速度,并带有估算的偏差补偿：")
                .append(values[4])
                .append("\n沿 z 轴的加速度,并带有估算的偏差补偿：")
                .append(values[5])
            txt2.text = "加速度传感器(有关偏差补偿)：$sb"
        }
        timerGravity.setTimerCallback { values ->
            Log.d(MOTION_SENSORS, "重力加速度传感器")
            sb = StringBuilder()
                .append("\n沿 x 轴的重力：")
                .append(values[0])
                .append("\n沿 y 轴的重力：")
                .append(values[1])
                .append("\n沿 z 轴的重力：")
                .append(values[2])
            txt3.text = "重力加速度传感器：$sb"
        }
        timerGyroscope.setTimerCallback { values ->
            Log.d(MOTION_SENSORS, "旋转速率传感器")
            sb = StringBuilder()
                .append("\n绕 x 轴的旋转速率：")
                .append(values[0])
                .append("\n绕 y 轴的旋转速率：")
                .append(values[1])
                .append("\n绕 z 轴的旋转速率：")
                .append(values[2])
            txt4.text = "旋转速率传感器：$sb"
        }
        timerGyroscopeUncalibrated.setTimerCallback { values ->
            Log.d(MOTION_SENSORS, "旋转速率传感器(有关漂移补偿)")
            sb = StringBuilder()
                .append("\n绕 x 轴的旋转速率(无漂移补偿)：")
                .append(values[0])
                .append("\n绕 y 轴的旋转速率(无漂移补偿)：")
                .append(values[1])
                .append("\n绕 z 轴的旋转速率(无漂移补偿)：")
                .append(values[2])
                .append("\n绕 x 轴的旋转速率(估算漂移补偿)：")
                .append(values[3])
                .append("\n绕 y 轴的旋转速率(估算漂移补偿)：")
                .append(values[4])
                .append("\n绕 z 轴的旋转速率(估算漂移补偿)：")
                .append(values[5])
            txt5.text = "旋转速率传感器(有关漂移补偿)：$sb"
        }
        timerLinearAcceleration.setTimerCallback { values ->
            Log.d(MOTION_SENSORS, "线性加速度传感器(不包含重力)")
            sb = StringBuilder()
                .append("\n沿 x 轴的加速度(不包含重力)：")
                .append(values[0])
                .append("\n沿 y 轴的加速度(不包含重力)：")
                .append(values[1])
                .append("\n沿 z 轴的加速度(不包含重力)：")
                .append(values[2])
            txt6.text = "线性加速度传感器(不包含重力)：$sb"
        }
        timerRotationVector.setTimerCallback { values ->
            Log.d(MOTION_SENSORS, "三轴旋转矢量分量")
            sb = StringBuilder()
                .append("\n沿 x 轴的旋转矢量分量 (x * sin(θ/2))：")
                .append(values[0])
                .append("\n沿 y 轴的旋转矢量分量 (y * sin(θ/2))：")
                .append(values[1])
                .append("\n沿 z 轴的旋转矢量分量 (z * sin(θ/2))：")
                .append(values[2])
                .append("\n旋转矢量的标量分量 ((cos(θ/2))：")
                .append(values[3])
            txt7.text = "三轴旋转矢量分量：$sb"
        }

        timerGameRotationVector.setTimerCallback { values ->
            Log.d(POSITION_SENSORS, "游戏旋转矢量传感器")
            sb = StringBuilder()
                .append("\n沿 x 轴的旋转矢量分量 (x * sin(θ/2))：")
                .append(values[0])
                .append("\n沿 y 轴的旋转矢量分量 (y * sin(θ/2))：")
                .append(values[1])
                .append("\n沿 z 轴的旋转矢量分量 (z * sin(θ/2))：")
                .append(values[2])
            txt8.text = "游戏旋转矢量传感器：$sb"
        }
        timerGeomagneticRotationVector.setTimerCallback { values ->
            Log.d(POSITION_SENSORS, "地磁旋转矢量传感器")
            sb = StringBuilder()
                .append("\n沿 x 轴的旋转矢量分量 (x * sin(θ/2))：")
                .append(values[0])
                .append("\n沿 y 轴的旋转矢量分量 (y * sin(θ/2))：")
                .append(values[1])
                .append("\n沿 z 轴的旋转矢量分量 (z * sin(θ/2))：")
                .append(values[2])
            txt9.text = "地磁旋转矢量传感器：$sb"
        }
        timerMagneticField.setTimerCallback { values ->
            Log.d(POSITION_SENSORS, "地磁场强度传感器")
            sb = StringBuilder()
                .append("\n沿 x 轴的地磁场强度：")
                .append(values[0])
                .append("\n沿 y 轴的地磁场强度：")
                .append(values[1])
                .append("\n沿 z 轴的地磁场强度：")
                .append(values[2])
            txt10.text = "地磁场强度传感器：$sb"
        }
        timerMagneticFieldUncalibrated.setTimerCallback { values ->
            Log.d(POSITION_SENSORS, "地磁场强度传感器(有关硬铁校准)")
            sb = StringBuilder()
                .append("\n沿 x 轴的地磁场强度(无硬铁校准功能)：")
                .append(values[0])
                .append("\n沿 y 轴的地磁场强度(无硬铁校准功能)：")
                .append(values[1])
                .append("\n沿 z 轴的地磁场强度(无硬铁校准功能)：")
                .append(values[2])
                .append("\n沿 x 轴的铁偏差估算：")
                .append(values[3])
                .append("\n沿 y 轴的铁偏差估算：")
                .append(values[4])
                .append("\n沿 z 轴的铁偏差估算：")
                .append(values[5])
            txt11.text = "地磁场强度传感器(有关硬铁校准)：$sb"
        }
        timerOrientation.setTimerCallback { values ->
            Log.d(POSITION_SENSORS, "三轴角度")
            sb = StringBuilder()
                .append("\n方位角(绕 z 轴的角度)：")
                .append(values[0])
                .append("\n俯仰角(绕 x 轴的角度)：")
                .append(values[1])
                .append("\n倾侧角(绕 y 轴的角度)：")
                .append(values[2])
            txt12.text = "三轴角度：$sb"
        }
        timerProximity.setTimerCallback { values ->
            Log.d(POSITION_SENSORS, "与物体距离 ")
            sb = StringBuilder()
                .append("\n")
                .append(values[0])
            txt13.text = "与物体距离：$sb"
        }
    }

    inner class SensorEventListenerImpl : SensorEventListener {
        // 传感器的值发生变化时触发
        override fun onSensorChanged(event: SensorEvent?) {
            val values = event?.values ?: return
            val sensorType = event.sensor?.type ?: return
            when (sensorType) {
                Sensor.TYPE_ACCELEROMETER ->
                    timerAccelerometer.record(values)

                Sensor.TYPE_ACCELEROMETER_UNCALIBRATED ->
                    timerAccelerometerUncalibrated.record(values)

                Sensor.TYPE_GRAVITY ->
                    timerGravity.record(values)

                Sensor.TYPE_GYROSCOPE ->
                    timerGyroscope.record(values)

                Sensor.TYPE_GYROSCOPE_UNCALIBRATED ->
                    timerGyroscopeUncalibrated.record(values)

                Sensor.TYPE_LINEAR_ACCELERATION ->
                    timerLinearAcceleration.record(values)

                Sensor.TYPE_ROTATION_VECTOR ->
                    timerRotationVector.record(values)

                Sensor.TYPE_GAME_ROTATION_VECTOR ->
                    timerGameRotationVector.record(values)

                Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR ->
                    timerGeomagneticRotationVector.record(values)

                Sensor.TYPE_MAGNETIC_FIELD ->
                    timerMagneticField.record(values)

                Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED ->
                    timerMagneticFieldUncalibrated.record(values)

                Sensor.TYPE_ORIENTATION ->
                    timerOrientation.record(values)

                Sensor.TYPE_PROXIMITY ->
                    timerProximity.record(values)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }
    }
}
