package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit

typealias DecayFunction = (power: Double, current: Double, limit: Double) -> Double

class MonitoredMotor(hw: HardwareMap, name: String, val limit: Double, val decay: DecayFunction) {
    val stuff = mutableListOf(0.0)
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
        print("suggested: $suggested, current: $current, limit: $limit, decayed: ${decay(suggested, current, limit)}")
        suggested = decay(suggested, current, limit)
        inner.power = suggested
        stuff.add(inner.power)
    }
}
