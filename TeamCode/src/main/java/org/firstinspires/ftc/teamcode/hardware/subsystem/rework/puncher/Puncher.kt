package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.abs

class Puncher(val hw: HardwareMap, val telemetry: Telemetry) : SubsystemBase() {
    private val puncher by lazy { hw["puncher"] as Servo }

    var state = State.NONE
    var count = 0

    fun to(state: State) { this.state = state }

    fun next() { this.state = State.values()[count % 3].also { count++ } }

    override fun periodic() {
        puncher.position = state.position

        telemetry.addData("puncher state", state.toString())
    }

    enum class State(val position: Double) {
        TWO(0.05),
        ONE(0.7),
        NONE(1.0)
    }
}