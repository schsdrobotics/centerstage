package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit

typealias DecayFunction = (power: Double, current: Double, limit: Double) -> Double

class MonitoredMotor(hw: HardwareMap, name: String, val limit: Double, val decay: DecayFunction) {
    private val inner = hw.dcMotor[name] as DcMotorEx

    private var suggested = 0.0

    val limited
        get() = current > limit

    val current
        get() = inner.getCurrent(CurrentUnit.MILLIAMPS)

    var power
        get() = inner.power
        set(value) { suggested = value }

    fun tick() {
        power = decay(power, current, limit)
        inner.power = suggested
    }
}