package com.example.sensordemo.bean

data class PostData(
    val id: String, // 学号
    val time: Long, // 跑步时间
    val data: List<SensorData>?, // 传感器数据（五秒收集一次）
    val tag: Boolean // 标记数据状态
)

data class SensorData(
    var accelerometer: Accelerometer? = null, // 加速度（包含重力）
    var accNoBiasComp: AccNoBiasComp? = null, // 加速度（没有偏差补偿）
    val accWithEstBiasComp: AccWithEstBiasComp? = null, // 加速度（估算偏差补偿）
    var gravityAcceleration: GravityAcceleration? = null, // 重力加速度
    var rotationRate: RotationRate? = null, // 旋转速率
    var rotRateNoDriftComp: RotRateNoDriftComp? = null, // 旋转速率（无漂移补偿）
    val rotRateWithEstDriftComp: RotRateWithEstDriftComp? = null, // 旋转速率（估算漂移补偿）
    var linearAcceleration: LinearAcceleration? = null, // 线性加速度
    var rotationVectorComponent: RotationVectorComponent? = null, // 旋转矢量分量
    var gameRotationVector: GameRotationVector? = null, // 游戏旋转矢量
    var geomagneticRotationVector: GeomagneticRotationVector? = null, // 地磁旋转矢量
    var geomagneticIntensity: GeomagneticIntensity? = null, // 地磁强度
    var geoIntUncal: GeoIntUncal? = null, // 地磁强度无硬铁校准
    val geoIntHardBiasEst: GeoIntHardBiasEst? = null, // 地磁强度硬铁估算
    var orientation: Orientation? = null, // 方位角
    var proximity: Float? = null // 与物体距离
)

data class Accelerometer(
    val x: Float,
    val y: Float,
    val z: Float
)

data class AccNoBiasComp(
    val x: Float,
    val y: Float,
    val z: Float
)

data class AccWithEstBiasComp(
    val x: Float,
    val y: Float,
    val z: Float
)

data class GravityAcceleration(
    val x: Float,
    val y: Float,
    val z: Float
)

data class RotationRate(
    val x: Float,
    val y: Float,
    val z: Float
)

data class RotRateNoDriftComp(
    val x: Float,
    val y: Float,
    val z: Float
)

data class RotRateWithEstDriftComp(
    val x: Float,
    val y: Float,
    val z: Float
)

data class LinearAcceleration(
    val x: Float,
    val y: Float,
    val z: Float
)

data class RotationVectorComponent(
    val x: Float,
    val y: Float,
    val z: Float,
    val scalar: Float // 标量矢量
)

data class GameRotationVector(
    val x: Float,
    val y: Float,
    val z: Float
)

data class GeomagneticRotationVector(
    val x: Float,
    val y: Float,
    val z: Float
)

data class GeomagneticIntensity(
    val x: Float,
    val y: Float,
    val z: Float
)

data class GeoIntUncal(
    val x: Float,
    val y: Float,
    val z: Float
)

data class GeoIntHardBiasEst(
    val x: Float,
    val y: Float,
    val z: Float
)

data class Orientation(
    val azimuth: Float,
    val pitch: Float,
    val roll: Float
)