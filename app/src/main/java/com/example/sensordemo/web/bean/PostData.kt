package com.example.sensordemo.web.bean

import com.google.gson.annotations.SerializedName

private const val INIT_VALUE = -666.666F

/**
 * 英文版本
 */
data class PostData(
    val id: Long, // 学号
    val time: String, // 跑步时间
    val data: List<SensorData?>, // 传感器数据 (五秒收集一次)
    val tag: Boolean // 标记数据状态
)

data class SensorData(
    var time: Long = -1,
    var accelerometer: Accelerometer = Accelerometer(), // 加速度 (包含重力)
    var accNoBiasComp: AccNoBiasComp = AccNoBiasComp(), // 加速度 (没有偏差补偿)
    var accWithEstBiasComp: AccWithEstBiasComp = AccWithEstBiasComp(), // 加速度 (估算偏差补偿)
    var gravityAcceleration: GravityAcceleration = GravityAcceleration(), // 重力加速度
    var rotationRate: RotationRate = RotationRate(), // 旋转速率
    var rotRateNoDriftComp: RotRateNoDriftComp = RotRateNoDriftComp(), // 旋转速率 (无漂移补偿)
    var rotRateWithEstDriftComp: RotRateWithEstDriftComp = RotRateWithEstDriftComp(), // 旋转速率 (估算漂移补偿)
    var linearAcceleration: LinearAcceleration = LinearAcceleration(), // 线性加速度
    var rotationVectorComponent: RotationVectorComponent = RotationVectorComponent(), // 旋转矢量分量
    var gameRotationVector: GameRotationVector = GameRotationVector(), // 游戏旋转矢量
    var geomagneticRotationVector: GeomagneticRotationVector = GeomagneticRotationVector(), // 地磁旋转矢量
    var geomagneticIntensity: GeomagneticIntensity = GeomagneticIntensity(), // 地磁强度
    var geoIntUncal: GeoIntUncal = GeoIntUncal(), // 地磁强度无硬铁校准
    var geoIntHardBiasEst: GeoIntHardBiasEst = GeoIntHardBiasEst(), // 地磁强度硬铁估算
    var orientation: Orientation = Orientation(), // 方位角
    var proximity: Float = -1F // 与物体距离
)


/**
 * 中文版本
 */
data class _PostData(
    @SerializedName("学号")
    val id: Long, // 学号
    @SerializedName("运动总时长")
    val time: String, // 跑步时间
    @SerializedName("传感器数据")
    val data: List<SensorData>?, // 传感器数据 (五秒收集一次)
    @SerializedName("标记")
    val tag: Boolean // 标记数据状态
)

data class _SensorData(
    @SerializedName("时间点")
    var time: Long,
    @SerializedName("加速度 (包含重力)")
    var accelerometer: Accelerometer, // 加速度 (包含重力)
    @SerializedName("加速度 (没有偏差补偿)")
    var accNoBiasComp: AccNoBiasComp, // 加速度 (没有偏差补偿)
    @SerializedName("加速度 (估算偏差补偿)")
    var accWithEstBiasComp: AccWithEstBiasComp, // 加速度 (估算偏差补偿)
    @SerializedName("重力加速度")
    var gravityAcceleration: GravityAcceleration, // 重力加速度
    @SerializedName("旋转速率")
    var rotationRate: RotationRate, // 旋转速率
    @SerializedName("旋转速率 (无漂移补偿)")
    var rotRateNoDriftComp: RotRateNoDriftComp, // 旋转速率 (无漂移补偿)
    @SerializedName("旋转速率 (估算漂移补偿)")
    var rotRateWithEstDriftComp: RotRateWithEstDriftComp, // 旋转速率 (估算漂移补偿)
    @SerializedName("线性加速度")
    var linearAcceleration: LinearAcceleration, // 线性加速度
    @SerializedName("旋转矢量分量")
    var rotationVectorComponent: RotationVectorComponent, // 旋转矢量分量
    @SerializedName("游戏旋转矢量")
    var gameRotationVector: GameRotationVector, // 游戏旋转矢量
    @SerializedName("地磁旋转矢量")
    var geomagneticRotationVector: GeomagneticRotationVector, // 地磁旋转矢量
    @SerializedName("地磁强度")
    var geomagneticIntensity: GeomagneticIntensity, // 地磁强度
    @SerializedName("地磁强度无硬铁校准")
    var geoIntUncal: GeoIntUncal, // 地磁强度无硬铁校准
    @SerializedName("地磁强度硬铁估算")
    var geoIntHardBiasEst: GeoIntHardBiasEst, // 地磁强度硬铁估算
    @SerializedName("方位角")
    var orientation: Orientation, // 方位角
    @SerializedName("与物体距离")
    var proximity: Float // 与物体距离
)

data class Accelerometer(
    val x: Float = INIT_VALUE,
    val y: Float = INIT_VALUE,
    val z: Float = INIT_VALUE
)

data class AccNoBiasComp(
    val x: Float = INIT_VALUE,
    val y: Float = INIT_VALUE,
    val z: Float = INIT_VALUE
)

data class AccWithEstBiasComp(
    val x: Float = INIT_VALUE,
    val y: Float = INIT_VALUE,
    val z: Float = INIT_VALUE
)

data class GravityAcceleration(
    val x: Float = INIT_VALUE,
    val y: Float = INIT_VALUE,
    val z: Float = INIT_VALUE
)

data class RotationRate(
    val x: Float = INIT_VALUE,
    val y: Float = INIT_VALUE,
    val z: Float = INIT_VALUE
)

data class RotRateNoDriftComp(
    val x: Float = INIT_VALUE,
    val y: Float = INIT_VALUE,
    val z: Float = INIT_VALUE
)

data class RotRateWithEstDriftComp(
    val x: Float = INIT_VALUE,
    val y: Float = INIT_VALUE,
    val z: Float = INIT_VALUE
)

data class LinearAcceleration(
    val x: Float = INIT_VALUE,
    val y: Float = INIT_VALUE,
    val z: Float = INIT_VALUE
)

data class RotationVectorComponent(
    val x: Float = INIT_VALUE,
    val y: Float = INIT_VALUE,
    val z: Float = INIT_VALUE,
    val scalar: Float = INIT_VALUE// 标量矢量
)

data class GameRotationVector(
    val x: Float = INIT_VALUE,
    val y: Float = INIT_VALUE,
    val z: Float = INIT_VALUE
)

data class GeomagneticRotationVector(
    val x: Float = INIT_VALUE,
    val y: Float = INIT_VALUE,
    val z: Float = INIT_VALUE
)

data class GeomagneticIntensity(
    val x: Float = INIT_VALUE,
    val y: Float = INIT_VALUE,
    val z: Float = INIT_VALUE
)

data class GeoIntUncal(
    val x: Float = INIT_VALUE,
    val y: Float = INIT_VALUE,
    val z: Float = INIT_VALUE
)

data class GeoIntHardBiasEst(
    val x: Float = INIT_VALUE,
    val y: Float = INIT_VALUE,
    val z: Float = INIT_VALUE
)

data class Orientation(
    val azimuth: Float = INIT_VALUE,
    val pitch: Float = INIT_VALUE,
    val roll: Float = INIT_VALUE
)