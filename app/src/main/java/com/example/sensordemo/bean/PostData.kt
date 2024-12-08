package com.example.sensordemo.bean

data class PostData(
    val id: String, // 学号
    val time: String, // 跑步时间
    val data: List<Double?>, // 测试用
//    val data: List<SensorData?>, // 传感器数据（五秒收集一次）
    val tag: Boolean // 标记数据状态
)

data class SensorData(
    val time: Long,
    val accelerometer: Accelerometer, // 加速度（包含重力）
    val accNoBiasComp: AccNoBiasComp, // 加速度（没有偏差补偿）
    val accWithEstBiasComp: AccWithEstBiasComp, // 加速度（估算偏差补偿）
    val gravityAcceleration: GravityAcceleration, // 重力加速度
    val rotationRate: RotationRate, // 旋转速率
    val rotRateNoDriftComp: RotRateNoDriftComp, // 旋转速率（无漂移补偿）
    val rotRateWithEstDriftComp: RotRateWithEstDriftComp, // 旋转速率（估算漂移补偿）
    val linearAcceleration: LinearAcceleration, // 线性加速度
    val rotationVectorComponent: RotationVectorComponent, // 旋转矢量分量
    val gameRotationVector: GameRotationVector, // 游戏旋转矢量
    val geomagneticRotationVector: GeomagneticRotationVector, // 地磁旋转矢量
    val geomagneticIntensity: GeomagneticIntensity, // 地磁强度
    val geoIntUncal: GeoIntUncal, // 地磁强度无硬铁校准
    val geoIntHardBiasEst: GeoIntHardBiasEst, // 地磁强度硬铁估算
    val orientation: Orientation, // 方位角
    val proximity: Double // 与物体距离
)

data class Accelerometer(
    val x: Double,
    val y: Double,
    val z: Double
)

data class AccNoBiasComp(
    val x: Double,
    val y: Double,
    val z: Double
)

data class AccWithEstBiasComp(
    val x: Double,
    val y: Double,
    val z: Double
)

data class GravityAcceleration(
    val x: Double,
    val y: Double,
    val z: Double
)

data class RotationRate(
    val x: Double,
    val y: Double,
    val z: Double
)

data class RotRateNoDriftComp(
    val x: Double,
    val y: Double,
    val z: Double
)

data class RotRateWithEstDriftComp(
    val x: Double,
    val y: Double,
    val z: Double
)

data class LinearAcceleration(
    val x: Double,
    val y: Double,
    val z: Double
)

data class RotationVectorComponent(
    val x: Double,
    val y: Double,
    val z: Double,
    val scalar: Double // 标量矢量
)

data class GameRotationVector(
    val x: Double,
    val y: Double,
    val z: Double
)

data class GeomagneticRotationVector(
    val x: Double,
    val y: Double,
    val z: Double
)

data class GeomagneticIntensity(
    val x: Double,
    val y: Double,
    val z: Double
)

data class GeoIntUncal(
    val x: Double,
    val y: Double,
    val z: Double
)

data class GeoIntHardBiasEst(
    val x: Double,
    val y: Double,
    val z: Double
)

data class Orientation(
    val azimuth: Double,
    val pitch: Double,
    val roll: Double
)