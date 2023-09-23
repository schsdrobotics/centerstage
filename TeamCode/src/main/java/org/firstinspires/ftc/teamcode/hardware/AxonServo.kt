package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.ServoImplEx

class AxonServo(hw: HardwareMap, private val name: String) {
    private val pot: AnalogInput by lazy { hw["$name pot"] as AnalogInput }
    private val inner: ServoImplEx by lazy { hw[name] as ServoImplEx }

    val servo
        get() = inner

    var position
        get() = pot.voltage / 3.3 * 360.0
        set(value) { inner.position = value }

    fun release() = inner.setPwmDisable()
    fun hold() = inner.setPwmEnable()
}