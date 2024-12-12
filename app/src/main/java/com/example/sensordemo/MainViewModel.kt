package com.example.sensordemo

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensordemo.util.MOTION_SENSORS
import com.example.sensordemo.util.POSITION_SENSORS
import com.example.sensordemo.util.SensorRecordTimer
import com.example.sensordemo.util.parseToPrettyJson
import com.example.sensordemo.web.bean.AccNoBiasComp
import com.example.sensordemo.web.bean.AccWithEstBiasComp
import com.example.sensordemo.web.bean.Accelerometer
import com.example.sensordemo.web.bean.GameRotationVector
import com.example.sensordemo.web.bean.GeoIntHardBiasEst
import com.example.sensordemo.web.bean.GeoIntUncal
import com.example.sensordemo.web.bean.GeomagneticIntensity
import com.example.sensordemo.web.bean.GeomagneticRotationVector
import com.example.sensordemo.web.bean.GravityAcceleration
import com.example.sensordemo.web.bean.LinearAcceleration
import com.example.sensordemo.web.bean.Orientation
import com.example.sensordemo.web.bean.PostData
import com.example.sensordemo.web.bean.RotRateNoDriftComp
import com.example.sensordemo.web.bean.RotRateWithEstDriftComp
import com.example.sensordemo.web.bean.RotationRate
import com.example.sensordemo.web.bean.RotationVectorComponent
import com.example.sensordemo.web.bean.SensorData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {
    companion object {
        private const val REFRESH_CD = 30L // 刷新间隔
        private const val CHECK_CD = 100L // 检查间隔

        private const val INIT_CD = 500L
    }

    private val _timeString = MutableLiveData("00:00:00.000")
    val timeString: LiveData<String>
        get() = _timeString

    private val _cdString = MutableLiveData("当前记录间隔: 每${INIT_CD}毫秒\n点击可以设置")
    val cdString: LiveData<String>
        get() = _cdString

    private val timerAccelerometer = SensorRecordTimer(INIT_CD)
    private val timerAccelerometerUncalibrated = SensorRecordTimer(INIT_CD)
    private val timerGravity = SensorRecordTimer(INIT_CD)
    private val timerGyroscope = SensorRecordTimer(INIT_CD)
    private val timerGyroscopeUncalibrated = SensorRecordTimer(INIT_CD)
    private val timerLinearAcceleration = SensorRecordTimer(INIT_CD)
    private val timerRotationVector = SensorRecordTimer(INIT_CD)
    private val timerGameRotationVector = SensorRecordTimer(INIT_CD)
    private val timerGeomagneticRotationVector = SensorRecordTimer(INIT_CD)
    private val timerMagneticField = SensorRecordTimer(INIT_CD)
    private val timerMagneticFieldUncalibrated = SensorRecordTimer(INIT_CD)
    private val timerOrientation = SensorRecordTimer(INIT_CD)
    private val timerProximity = SensorRecordTimer(INIT_CD)

    var CD = INIT_CD // 收集数据的间隔时间
        set(value) {
            if (value !in 1L..10000L) return // 限定在合理的范围内
            _cdString.value = "当前记录间隔: 每${value}毫秒\n点击可以设置"
            timerAccelerometer.cd = value
            timerAccelerometerUncalibrated.cd = value
            timerGravity.cd = value
            timerGyroscope.cd = value
            timerGyroscopeUncalibrated.cd = value
            timerLinearAcceleration.cd = value
            timerRotationVector.cd = value
            timerGameRotationVector.cd = value
            timerGeomagneticRotationVector.cd = value
            timerMagneticField.cd = value
            timerMagneticFieldUncalibrated.cd = value
            timerOrientation.cd = value
            timerProximity.cd = value
            resetTimer()
            cleanDataList()
            field = value
        }

    private val sensorData = SensorData()

    private var startTime: Long = 0
    private var elapsedTime: Long = 0

    private var timerJob: Job? = null

    private var _isRunning: Boolean = false
    val isRunning: Boolean
        get() = _isRunning

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
                        sensorDataList.add(sensorData)
                        lastRecordTime = System.currentTimeMillis()
                    }
                    delay(CHECK_CD)
                }
            }
        }
    }

    fun getPostData(id: Long): PostData {
        val time = elapsedTime.toTimeFormat(false)

        return PostData(
            id,
            time,
            sensorDataList,
            true
        )
    }

    fun postJsonData(id: Long, callback: (Boolean, String) -> Unit) {
        if (sensorDataList.isEmpty()) {
            callback(false, "未收集到数据")
            return
        }
        val postData = getPostData(id)

        mainModel.postJsonData(
            postData,
            { _ ->
                viewModelScope.parseToPrettyJson(postData) {
                    callback(true, it)
                }
                Log.e("XXXX", "ok")
            }, // on success
            { code, mg ->
                val msg = code ?: "无状态码, 问题是: $mg"
                Log.e("XXXX", mg)
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
                _timeString.value = elapsedTime.toTimeFormat(true)
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

    /**
     * 分为是否带毫秒
     */
    private fun Long.toTimeFormat(withMS: Boolean): String {
        val hours = TimeUnit.MILLISECONDS.toHours(this)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(this) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60
        val milliseconds = this % 1000

        if (withMS) {
            return String.format(
                Locale.CHINA,
                "%02d:%02d:%02d.%03d",
                hours,
                minutes,
                seconds, milliseconds
            )
        } else {
            return String.format(
                Locale.CHINA,
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds
            )
        }
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