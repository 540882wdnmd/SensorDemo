package com.example.sensordemo

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


//实现接口SensorEventListener
class MainActivity : AppCompatActivity(), SensorEventListener {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        //2.实例化SensorManager，获取传感器服务
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        //获取当前手机支持哪些传感器
        val list = sensorManager!!.getSensorList(Sensor.TYPE_ALL)
        for (i in list.indices) {
            Log.d("bbb", list[i].name)
        }
    }

    override fun onResume() {
        super.onResume()

        //3.在onResume中向SensorManager注册传感器

        //加速度传感器
        //加速度传感器=重力传感器+线性加速度传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_UI
        )
        //磁场传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        //方向传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        //陀螺仪传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        //光照传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        //气压传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_PRESSURE),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        //手机内部温度传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_TEMPERATURE),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        //距离传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        //重力传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_GRAVITY),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        //线性加速度传感器
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
        //湿度传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        //手机外部温度传感器
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
            SensorManager.SENSOR_DELAY_NORMAL
        )
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
                Log.e("aaa", "This is " + "加速度传感器")
                sb = StringBuilder()
                sb.append("\n")
                sb.append("X方向上的加速度：")
                sb.append(values[0])
                sb.append("\nY方向上的加速度：")
                sb.append(values[1])
                sb.append("\nZ方向上的加速度：")
                sb.append(values[2])
                txt1!!.text = "加速度传感器：$sb"
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                Log.e("aaa", "This is " + "磁场传感器")
                sb = StringBuilder()
                sb.append("\n")
                sb.append("X方向上的磁场强度：")
                sb.append(values[0])
                sb.append("\nY方向上的磁场强度：")
                sb.append(values[1])
                sb.append("\nZ方向上的磁场强度：")
                sb.append(values[2])
                txt2!!.text = "磁场传感器：$sb"
            }

            Sensor.TYPE_ORIENTATION -> {
                Log.e("aaa", "This is " + "方向传感器")
                sb = StringBuilder()
                sb.append("\n")
                sb.append("X方向上的转过的角度：")
                sb.append(values[0])
                sb.append("\nY方向上的转过的角度：")
                sb.append(values[1])
                sb.append("\nZ方向上的转过的角度：")
                sb.append(values[2])
                txt3!!.text = "方向传感器：$sb"
            }

            Sensor.TYPE_GYROSCOPE -> {
                Log.e("aaa", "This is " + "陀螺仪传感器")
                sb = StringBuilder()
                sb.append("\n")
                sb.append("X方向上的角速度：")
                sb.append(values[0])
                sb.append("\nY方向上的角速度：")
                sb.append(values[1])
                sb.append("\nZ方向上的角速度：")
                sb.append(values[2])
                txt4!!.text = "陀螺仪传感器：$sb"
            }

            Sensor.TYPE_LIGHT -> {
                Log.e("aaa", "This is " + "光照传感器")
                sb = StringBuilder()
                sb.append("\n")
                sb.append("当前光的强度为：")
                sb.append(values[0])
                txt5!!.text = "光照传感器：$sb"
            }

            /*
            没有显示
             */
            Sensor.TYPE_PRESSURE -> {
                Log.e("aaa", "This is " + "压力传感器")
                sb = StringBuilder()
                sb.append("\n")
                sb.append("当前压力为：")
                sb.append(values[0])
                txt6!!.text = "压力传感器：$sb"
            }

            /*
            没有显示
             */
            Sensor.TYPE_TEMPERATURE -> {
                Log.e("aaa", "This is " + "手机内部温度传感器")
                sb = StringBuilder()
                sb.append("\n")
                sb.append("当前温度为：")
                sb.append(values[0])
                txt7!!.text = "手机内部温度传感器：$sb"
            }

            /*
            长度问题
             */
            Sensor.TYPE_PROXIMITY -> {
                Log.e("aaa", "This is " + "距离传感器")
                sb = StringBuilder()
                sb.append("\n")
                sb.append("X方向上的距离：")
                sb.append(values[0])
//                sb.append("\nY方向上的距离：")
//                sb.append(values[1])
//                sb.append("\nZ方向上的距离：")
//                sb.append(values[2])
                txt8!!.text = "距离传感器：$sb"
            }


            Sensor.TYPE_GRAVITY -> {
                Log.e("aaa", "This is " + "重力传感器")
                sb = StringBuilder()
                sb.append("\n")
                sb.append("X方向上的重力：")
                sb.append(values[0])
                sb.append("\nY方向上的重力：")
                sb.append(values[1])
                sb.append("\nZ方向上的重力：")
                sb.append(values[2])
                txt9!!.text = "重力传感器：$sb"
            }

            Sensor.TYPE_LINEAR_ACCELERATION -> {
                Log.e("aaa", "This is " + "线性加速度传感器")
                sb = StringBuilder()
                sb.append("\n")
                sb.append("X方向上的线性加速度：")
                sb.append(values[0])
                sb.append("\nY方向上的线性加速度：")
                sb.append(values[1])
                sb.append("\nZ方向上的线性加速度：")
                sb.append(values[2])
                txt10!!.text = "线性加速度传感器：$sb"
            }

            Sensor.TYPE_ROTATION_VECTOR -> {
                Log.e("aaa", "This is " + "旋转矢量传感器")
                sb = StringBuilder()
                sb.append("\n")
                sb.append("X方向上的欧拉角：")
                sb.append(values[0])
                sb.append("\nY方向上的欧拉角：")
                sb.append(values[1])
                sb.append("\nZ方向上的欧拉角：")
                sb.append(values[2])
                txt11!!.text = "旋转矢量传感器：$sb"
            }

            /*
            没有显示
             */
            Sensor.TYPE_RELATIVE_HUMIDITY -> {
                Log.e("aaa", "This is " + "湿度传感器")
                sb = StringBuilder()
                sb.append("\n")
                sb.append("当前湿度值：")
                sb.append(values[0])
                txt12!!.text = "湿度传感器：$sb"
            }

            /*
            没有显示
             */
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                Log.e("aaa", "This is " + "手机外部温度传感器")
                sb = StringBuilder()
                sb.append("\n")
                sb.append("当前温度值：")
                sb.append(values[0])
                txt13!!.text = "手机外部温度传感器：$sb"
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
    }
}
