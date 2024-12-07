package com.example.sensordemo

import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.widget.Toast


//实现接口SensorEventListener
class TestActivity : AppCompatActivity(), SensorEventListener {
    private var sensorManager: SensorManager? = null //1.在类中创建SensorManager传感器管理器

    private var txt1: TextView? = null
    private var txt2: TextView? = null
    private var txt3: TextView? = null
    private var txt4: TextView? = null
    private var txt5: TextView? = null
    private var txt6: TextView? = null
    private var txt7: TextView? = null
    private var txt8: TextView? = null
    private var txt9: TextView? = null
    private var txt10: TextView? = null
    private var txt11: TextView? = null
    private var txt12: TextView? = null
    private var txt13: TextView? = null
    private var txt14: TextView? = null
    private var txt15: TextView? = null
    private var txt16: TextView? = null
    private var txt17: TextView? = null
    private var txt18: TextView? = null
    private var txt19: TextView? = null

    companion object{
        const val ALL_SENSORS = "All_Sensors"
        const val MOTION_SENSORS = "Motion_Sensors"
        const val POSITION_SENSORS = "Position_Sensors"
        const val ENVIRONMENT_SENSORS = "Environment_Sensors"
        const val SUPPORT = "Support"
        const val PERMISSION_REQUEST_CODE = 100
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initView()
        //2.实例化SensorManager，获取传感器服务
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        //获取当前手机支持哪些传感器
        val list = sensorManager!!.getSensorList(Sensor.TYPE_ALL)
        for (i in list.indices) {
            Log.d(ALL_SENSORS, list[i].name)
        }
        Log.d(SUPPORT,"是否支持StepCounter:"+ packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER).toString())
        Log.d(SUPPORT,"是否支持StepDetector:"+ packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR).toString())
        Log.d(SUPPORT,"是否支持AMBIENT_TEMPERATURE:"+ packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE).toString())

        val permission = Manifest.permission.ACTIVITY_RECOGNITION
        val permissionGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted) {
            // 已经授予权限，执行相关操作
            Toast.makeText(this,"获取权限成功",Toast.LENGTH_SHORT).show()
        } else {
            // 未授予权限，请求权限
            ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_REQUEST_CODE)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"获取权限成功",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"获取权限失败无法获取步数",Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        //3.在onResume中向SensorManager注册传感器

        /*
        移动传感器(Motion Sensors)
         */
        //加速度传感器
        //加速度传感器=重力传感器+线性加速度传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_UI
        )

        //有关偏差补偿的三轴加速度传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED),
            SensorManager.SENSOR_DELAY_UI
        )

        //重力传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_GRAVITY),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        //陀螺仪传感器 有关旋转速度的传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        //有关漂移补偿三轴的旋转加速器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        //线性加速度传感器(即不包含重力的加速器)
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        //旋转矢量传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        //显著运动传感器(用来激活传感器的一个常量)
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        //步数记录传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
            SensorManager.SENSOR_DELAY_FASTEST
        )


        //步数检测传感器(辅助计算的一个传感器)
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
            SensorManager.SENSOR_DELAY_FASTEST
        )

        /*
        位置传感器(Position Sensors)
         */
        //游戏旋转矢量传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        //地磁旋转矢量传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        //三轴的地磁场强度传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        //有关硬铁校准功能的三轴的地磁场强度传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        //有关角度的三轴传感器(此方法已在API15弃用)
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        //与物体距离
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        /*
        环境传感器
         */

        //环境空气温度
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        //光照强度
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        //环境气压强度
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_PRESSURE),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        //环境相对湿度
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY),
            SensorManager.SENSOR_DELAY_NORMAL
        )

//        //设备温度(API15已弃用)
//        sensorManager!!.registerListener(
//            this,
//            sensorManager!!.getDefaultSensor(Sensor.TYPE_TEMPERATURE),
//            SensorManager.SENSOR_DELAY_NORMAL
//        )

    }

    override fun onStop() {
        //在super前调用，表示先清空子类，再清空父类
        sensorManager!!.unregisterListener(this)
        super.onStop()
    }

    //传感器的值发生变化时触发
    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val values = sensorEvent.values
        val sensorType = sensorEvent.sensor.type
        val sb: StringBuilder
        when (sensorType) {
            Sensor.TYPE_ACCELEROMETER -> {
                Log.d(MOTION_SENSORS, "加速度传感器")
                sb = StringBuilder()
                sb.append("\n沿 x 轴的加速度：")
                sb.append(values[0])
                sb.append("\n沿 y 轴的加速度：")
                sb.append(values[1])
                sb.append("\n沿 z 轴的加速度：")
                sb.append(values[2])
                txt1!!.text = "加速度传感器(包含重力)：$sb"
            }

            Sensor.TYPE_ACCELEROMETER_UNCALIBRATED -> {
                Log.d(MOTION_SENSORS, "加速度传感器(有关偏差补偿)")
                sb = StringBuilder()
                sb.append("\n沿 x 轴的加速度,没有任何偏差补偿：")
                sb.append(values[0])
                sb.append("\n沿 y 轴的加速度,没有任何偏差补偿：")
                sb.append(values[1])
                sb.append("\n沿 z 轴的加速度,没有任何偏差补偿：")
                sb.append(values[2])
                sb.append("\n沿 x 轴的加速度,并带有估算的偏差补偿：")
                sb.append(values[3])
                sb.append("\n沿 y 轴的加速度,并带有估算的偏差补偿：")
                sb.append(values[4])
                sb.append("\n沿 z 轴的加速度,并带有估算的偏差补偿：")
                sb.append(values[5])
                txt2!!.text = "加速度传感器(有关偏差补偿)：$sb"
            }

            Sensor.TYPE_GRAVITY -> {
                Log.d(MOTION_SENSORS, "重力加速度传感器")
                sb = StringBuilder()
                sb.append("\n沿 x 轴的重力：")
                sb.append(values[0])
                sb.append("\n沿 y 轴的重力：")
                sb.append(values[1])
                sb.append("\n沿 z 轴的重力：")
                sb.append(values[2])
                txt3!!.text = "重力加速度传感器：$sb"
            }

            Sensor.TYPE_GYROSCOPE -> {
                Log.d(MOTION_SENSORS, "旋转速率传感器")
                sb = StringBuilder()
                sb.append("\n绕 x 轴的旋转速率：")
                sb.append(values[0])
                sb.append("\n绕 y 轴的旋转速率：")
                sb.append(values[1])
                sb.append("\n绕 z 轴的旋转速率：")
                sb.append(values[2])
                txt4!!.text = "旋转速率传感器：$sb"
            }

            Sensor.TYPE_GYROSCOPE_UNCALIBRATED -> {
                Log.d(MOTION_SENSORS, "旋转速率传感器(有关漂移补偿)")
                sb = StringBuilder()
                sb.append("\n绕 x 轴的旋转速率(无漂移补偿)：")
                sb.append(values[0])
                sb.append("\n绕 y 轴的旋转速率(无漂移补偿)：")
                sb.append(values[1])
                sb.append("\n绕 z 轴的旋转速率(无漂移补偿)：")
                sb.append(values[2])
                sb.append("\n绕 x 轴的旋转速率(估算漂移补偿)：")
                sb.append(values[3])
                sb.append("\n绕 y 轴的旋转速率(估算漂移补偿)：")
                sb.append(values[4])
                sb.append("\n绕 z 轴的旋转速率(估算漂移补偿)：")
                sb.append(values[5])
                txt5!!.text = "旋转速率传感器(有关漂移补偿)：$sb"
            }

            Sensor.TYPE_LINEAR_ACCELERATION -> {
                Log.d(MOTION_SENSORS, "线性加速度传感器(不包含重力)")
                sb = StringBuilder()
                sb.append("\n沿 x 轴的加速度(不包含重力)：")
                sb.append(values[0])
                sb.append("\n沿 y 轴的加速度(不包含重力)：")
                sb.append(values[1])
                sb.append("\n沿 z 轴的加速度(不包含重力)：")
                sb.append(values[2])
                txt6!!.text = "线性加速度传感器(不包含重力)：$sb"
            }

            Sensor.TYPE_ROTATION_VECTOR -> {
                Log.d(MOTION_SENSORS, "三轴旋转矢量分量")
                sb = StringBuilder()
                sb.append("\n沿 x 轴的旋转矢量分量 (x * sin(θ/2))：")
                sb.append(values[0])
                sb.append("\n沿 y 轴的旋转矢量分量 (y * sin(θ/2))：")
                sb.append(values[1])
                sb.append("\n沿 z 轴的旋转矢量分量 (z * sin(θ/2))：")
                sb.append(values[2])
                sb.append("\n旋转矢量的标量分量 ((cos(θ/2))：")
                sb.append(values[3])
                txt7!!.text = "三轴旋转矢量分量：$sb"
            }

            Sensor.TYPE_STEP_COUNTER -> {
                Log.d(MOTION_SENSORS, "已激活传感器最后一次重新启动以来用户迈出的步数 ")
                sb = StringBuilder()
                sb.append("\n")
                sb.append(values[0])
                txt8!!.text = "已激活传感器最后一次重新启动以来用户迈出的步数：$sb"
            }

            Sensor.TYPE_GAME_ROTATION_VECTOR -> {
                Log.d(POSITION_SENSORS, "游戏旋转矢量传感器")
                sb = StringBuilder()
                sb.append("\n沿 x 轴的旋转矢量分量 (x * sin(θ/2))：")
                sb.append(values[0])
                sb.append("\n沿 y 轴的旋转矢量分量 (y * sin(θ/2))：")
                sb.append(values[1])
                sb.append("\n沿 z 轴的旋转矢量分量 (z * sin(θ/2))：")
                sb.append(values[2])
                txt9!!.text = "游戏旋转矢量传感器：$sb"
            }

            Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR -> {
                Log.d(POSITION_SENSORS, "地磁旋转矢量传感器")
                sb = StringBuilder()
                sb.append("\n沿 x 轴的旋转矢量分量 (x * sin(θ/2))：")
                sb.append(values[0])
                sb.append("\n沿 y 轴的旋转矢量分量 (y * sin(θ/2))：")
                sb.append(values[1])
                sb.append("\n沿 z 轴的旋转矢量分量 (z * sin(θ/2))：")
                sb.append(values[2])
                txt10!!.text = "地磁旋转矢量传感器：$sb"
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                Log.d(POSITION_SENSORS, "地磁场强度传感器")
                sb = StringBuilder()
                sb.append("\n沿 x 轴的地磁场强度：")
                sb.append(values[0])
                sb.append("\n沿 x 轴的地磁场强度：")
                sb.append(values[1])
                sb.append("\n沿 x 轴的地磁场强度：")
                sb.append(values[2])
                txt11!!.text = "地磁场强度传感器：$sb"
            }

            Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED -> {
                Log.d(POSITION_SENSORS, "地磁场强度传感器(有关硬铁校准)")
                sb = StringBuilder()
                sb.append("\n沿 x 轴的地磁场强度(无硬铁校准功能)：")
                sb.append(values[0])
                sb.append("\n沿 y 轴的地磁场强度(无硬铁校准功能)：")
                sb.append(values[1])
                sb.append("\n沿 z 轴的地磁场强度(无硬铁校准功能)：")
                sb.append(values[2])
                sb.append("\n沿 x 轴的铁偏差估算：")
                sb.append(values[3])
                sb.append("\n沿 y 轴的铁偏差估算：")
                sb.append(values[4])
                sb.append("\n沿 z 轴的铁偏差估算：")
                sb.append(values[5])
                txt12!!.text = "地磁场强度传感器(有关硬铁校准)：$sb"
            }

            Sensor.TYPE_ORIENTATION -> {
                Log.d(POSITION_SENSORS, "三轴角度")
                sb = StringBuilder()
                sb.append("\n方位角(绕 z 轴的角度)：")
                sb.append(values[0])
                sb.append("\n俯仰角(绕 x 轴的角度)：")
                sb.append(values[1])
                sb.append("\n倾侧角(绕 y 轴的角度)：")
                sb.append(values[2])
                txt13!!.text = "三轴角度：$sb"
            }

            Sensor.TYPE_PROXIMITY-> {
                Log.d(POSITION_SENSORS, "与物体距离 ")
                sb = StringBuilder()
                sb.append("\n")
                sb.append(values[0])
                txt14!!.text = "与物体距离：$sb"
            }

            Sensor.TYPE_AMBIENT_TEMPERATURE-> {
                Log.d(ENVIRONMENT_SENSORS, "环境空气温度 ")
                sb = StringBuilder()
                sb.append("\n")
                sb.append(values[0])
                txt15!!.text = "环境空气温度：$sb"
            }

            Sensor.TYPE_LIGHT-> {
                Log.d(ENVIRONMENT_SENSORS, "光照强度 ")
                sb = StringBuilder()
                sb.append("\n")
                sb.append(values[0])
                txt16!!.text = "光照强度：$sb"
            }

            Sensor.TYPE_PRESSURE-> {
                Log.d(ENVIRONMENT_SENSORS, "环境气压强度 ")
                sb = StringBuilder()
                sb.append("\n")
                sb.append(values[0])
                txt17!!.text = "环境气压强度：$sb"
            }

            Sensor.TYPE_RELATIVE_HUMIDITY-> {
                Log.d(ENVIRONMENT_SENSORS, "环境相对湿度 ")
                sb = StringBuilder()
                sb.append("\n")
                sb.append(values[0])
                txt18!!.text = "环境相对湿度：$sb"
            }

            Sensor.TYPE_TEMPERATURE-> {
                Log.d(ENVIRONMENT_SENSORS, "设备温度 ")
                sb = StringBuilder()
                sb.append("\n")
                sb.append(values[0])
                txt19!!.text = "设备温度：$sb"
            }

        }
    }

    //精度改变时触发
    override fun onAccuracyChanged(sensor: Sensor, i: Int) {
    }

    private fun initView() {
        txt1 = findViewById<View>(R.id.txt1) as TextView
        txt2 = findViewById<View>(R.id.txt2) as TextView
        txt3 = findViewById<View>(R.id.txt3) as TextView
        txt4 = findViewById<View>(R.id.txt4) as TextView
        txt5 = findViewById<View>(R.id.txt5) as TextView
        txt6 = findViewById<View>(R.id.txt6) as TextView
        txt7 = findViewById<View>(R.id.txt7) as TextView
        txt8 = findViewById<View>(R.id.txt8) as TextView
        txt9 = findViewById<View>(R.id.txt9) as TextView
        txt10 = findViewById<View>(R.id.txt10) as TextView
        txt11 = findViewById<View>(R.id.txt11) as TextView
        txt12 = findViewById<View>(R.id.txt12) as TextView
        txt13 = findViewById<View>(R.id.txt13) as TextView
        txt14 = findViewById<View>(R.id.txt14) as TextView
        txt15 = findViewById<View>(R.id.txt15) as TextView
        txt16 = findViewById<View>(R.id.txt16) as TextView
        txt17 = findViewById<View>(R.id.txt17) as TextView
        txt18 = findViewById<View>(R.id.txt18) as TextView
        txt19 = findViewById<View>(R.id.txt19) as TextView
    }
    
}
