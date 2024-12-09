package com.example.sensordemo

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensordemo.bean.AccNoBiasComp
import com.example.sensordemo.bean.AccWithEstBiasComp
import com.example.sensordemo.bean.Accelerometer
import com.example.sensordemo.bean.GameRotationVector
import com.example.sensordemo.bean.GeoIntHardBiasEst
import com.example.sensordemo.bean.GeoIntUncal
import com.example.sensordemo.bean.GeomagneticIntensity
import com.example.sensordemo.bean.GeomagneticRotationVector
import com.example.sensordemo.bean.GravityAcceleration
import com.example.sensordemo.bean.LinearAcceleration
import com.example.sensordemo.bean.Orientation
import com.example.sensordemo.bean.PostData
import com.example.sensordemo.bean.RotRateNoDriftComp
import com.example.sensordemo.bean.RotRateWithEstDriftComp
import com.example.sensordemo.bean.RotationRate
import com.example.sensordemo.bean.RotationVectorComponent
import com.example.sensordemo.bean.SensorData
import com.example.sensordemo.util.MOTION_SENSORS
import com.example.sensordemo.util.POSITION_SENSORS
import com.example.sensordemo.util.SensorRecordTimer
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {
    companion object {
        private const val REFRESH_CD = 10L // 刷新间隔
        private const val CHECK_CD = 100L // 检查间隔
        const val CD = 500L //收集数据的间隔时间
    }

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

    private val sensorData = SensorData()

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

    val listener = SensorEventListenerImpl()

    private var sensorDataList: MutableList<SensorData> = mutableListOf()

    init {
        initSensorTimer()
        viewModelScope.launch(Dispatchers.IO) {
            var lastRecordTime = 0L
            while (true) {
                if (isRunning) {
                    val now = System.currentTimeMillis()
                    if (now - lastRecordTime >= CD) {
                        sensorData.time = System.currentTimeMillis()
//                        sensorData.time = LocalTime.now().toString()
                        sensorDataList.add(sensorData)
                        lastRecordTime = System.currentTimeMillis()
                    }
                    delay(CHECK_CD)
                }
            }
        }
    }

    fun postJsonData(id: String, callback: (Boolean, String) -> Unit) {
        if (sensorDataList.isEmpty()) {
            callback(false, "未收集到数据")
            return
        }
        val postData = PostData(
            id,
            timeString.value.toString(),
            sensorDataList,
            true
        )
        mainModel.postJsonData(
            postData,
            { _ ->
                callback(true, Gson().toJson(postData))
            }, // on success
            { code, _ ->
                val msg = code ?: "请求超时"
                callback(false, msg.toString())
            } // on error. 状态码可空
        )
    }

    fun cleanDataList() {
        sensorDataList = mutableListOf()
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
        sensorDataList.clear()
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

    /*
      定时收集传感器数据
       */
    private fun initSensorTimer() {
        timerAccelerometer.setTimerCallback { values ->
            sensorData.accelerometer = Accelerometer(
                values[0],
                values[1],
                values[2]
            )
        }

        timerAccelerometerUncalibrated.setTimerCallback { values ->
            sensorData.accNoBiasComp = AccNoBiasComp(
                values[0],
                values[1],
                values[2]
            )
            sensorData.accWithEstBiasComp = AccWithEstBiasComp(
                values[3],
                values[4],
                values[5]
            )
        }

        timerGravity.setTimerCallback { values ->
            sensorData.gravityAcceleration = GravityAcceleration(
                values[0],
                values[1],
                values[2]
            )
        }

        timerGyroscope.setTimerCallback { values ->
            sensorData.rotationRate = RotationRate(
                values[0],
                values[1],
                values[2]
            )
        }

        timerGyroscopeUncalibrated.setTimerCallback { values ->
            sensorData.rotRateNoDriftComp = RotRateNoDriftComp(
                values[0],
                values[1],
                values[2]
            )
            sensorData.rotRateWithEstDriftComp = RotRateWithEstDriftComp(
                values[3],
                values[4],
                values[5]
            )
        }

        timerLinearAcceleration.setTimerCallback { values ->
            sensorData.linearAcceleration = LinearAcceleration(
                values[0],
                values[1],
                values[2]
            )
        }

        timerRotationVector.setTimerCallback { values ->
            sensorData.rotationVectorComponent = RotationVectorComponent(
                values[0],
                values[1],
                values[2],
                values[3]
            )
        }

        timerGameRotationVector.setTimerCallback { values ->
            sensorData.gameRotationVector = GameRotationVector(
                values[0],
                values[1],
                values[2]
            )
        }

        timerGeomagneticRotationVector.setTimerCallback { values ->
            sensorData.geomagneticRotationVector = GeomagneticRotationVector(
                values[0],
                values[1],
                values[2]
            )
        }

        timerMagneticField.setTimerCallback { values ->
            sensorData.geomagneticIntensity = GeomagneticIntensity(
                values[0],
                values[1],
                values[2]
            )
        }

        timerMagneticFieldUncalibrated.setTimerCallback { values ->
            sensorData.geoIntUncal = GeoIntUncal(
                values[0],
                values[1],
                values[2]
            )
            sensorData.geoIntHardBiasEst = GeoIntHardBiasEst(
                values[3],
                values[4],
                values[5]
            )
        }

        timerOrientation.setTimerCallback { values ->
            sensorData.orientation = Orientation(
                values[0],
                values[1],
                values[2]
            )
        }

        timerProximity.setTimerCallback { values ->
            sensorData.proximity = values[0]
        }
    }

    /*
    传感器数值发生改变的接口实现类
     */
    inner class SensorEventListenerImpl : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            val values = event?.values ?: return
            val sensorType = event.sensor?.type ?: return
            when (sensorType) {
                Sensor.TYPE_ACCELEROMETER -> {
                    Log.d(MOTION_SENSORS, "加速度传感器")
                    timerAccelerometer.record(values)
                }

                Sensor.TYPE_ACCELEROMETER_UNCALIBRATED -> {
                    Log.d(MOTION_SENSORS, "加速度传感器(有关偏差补偿)")
                    timerAccelerometerUncalibrated.record(values)
                }

                Sensor.TYPE_GRAVITY -> {
                    Log.d(MOTION_SENSORS, "重力加速度传感器")
                    timerGravity.record(values)
                }

                Sensor.TYPE_GYROSCOPE -> {
                    Log.d(MOTION_SENSORS, "旋转速率传感器")
                    timerGyroscope.record(values)
                }

                Sensor.TYPE_GYROSCOPE_UNCALIBRATED -> {
                    Log.d(MOTION_SENSORS, "旋转速率传感器(有关漂移补偿)")
                    timerGyroscopeUncalibrated.record(values)
                }

                Sensor.TYPE_LINEAR_ACCELERATION -> {
                    Log.d(MOTION_SENSORS, "线性加速度传感器(不包含重力)")
                    timerLinearAcceleration.record(values)
                }

                Sensor.TYPE_ROTATION_VECTOR -> {
                    Log.d(MOTION_SENSORS, "三轴旋转矢量分量")
                    timerRotationVector.record(values)
                }

                Sensor.TYPE_GAME_ROTATION_VECTOR -> {
                    Log.d(POSITION_SENSORS, "游戏旋转矢量传感器")
                    timerGameRotationVector.record(values)
                }

                Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR -> {
                    Log.d(POSITION_SENSORS, "地磁旋转矢量传感器")
                    timerGeomagneticRotationVector.record(values)
                }

                Sensor.TYPE_MAGNETIC_FIELD -> {
                    Log.d(POSITION_SENSORS, "地磁场强度传感器")
                    timerMagneticField.record(values)

                }

                Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED -> {
                    Log.d(POSITION_SENSORS, "地磁场强度传感器(有关硬铁校准)")
                    timerMagneticFieldUncalibrated.record(values)

                }

                Sensor.TYPE_ORIENTATION -> {
                    Log.d(POSITION_SENSORS, "三轴角度")
                    timerOrientation.record(values)

                }

                Sensor.TYPE_PROXIMITY -> {
                    Log.d(POSITION_SENSORS, "与物体距离 ")
                    timerProximity.record(values)

                }

            }


        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }
}