package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.CRServoImpl
import com.qualcomm.robotcore.hardware.HardwareMap

class ContinuousAxonServo(hw: HardwareMap, private val name: String) {
    private val pot: AnalogInput by lazy { hw["$name pot"] as AnalogInput }
    private val inner by lazy { hw[name] as CRServoImpl }

    val servo
        get() = inner

    val position
        get() = pot.voltage / 3.3 * 360.0

    var power
        get() = servo.power
        set(value) { servo.power = value }
}