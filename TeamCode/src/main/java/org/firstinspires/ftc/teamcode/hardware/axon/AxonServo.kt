package org.firstinspires.ftc.teamcode.hardware.axon

import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.ServoImplEx
import kotlin.math.abs

class AxonServo(hw: HardwareMap, private val name: String) {
    private val pot: AnalogInput by lazy { hw["$name pot"] as AnalogInput }
    private val inner by lazy { hw[name] as ServoImplEx }

    val servo
        get() = inner

    var position
        get() = abs(1 - (pot.voltage / 3.3))
        set(value) { inner.position = value }

    val degrees
        get() = position * 360.0

    fun scaleRange(min: Double, max: Double) = inner.scaleRange(min, max)

    fun release() = inner.setPwmDisable()
    fun hold() = inner.setPwmEnable()
}