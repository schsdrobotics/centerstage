package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher.State.NONE
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher.State.ONE
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher.State.TWO

class Puncher(val hw: HardwareMap, val telemetry: Telemetry, var state: State = NONE) : SubsystemBase() {
    private val puncher by lazy { hw["puncher"] as Servo }

    init { this.puncher.position = state.position }

    fun to(state: State) { this.state = state }

    fun next() { this.state = when (this.state) { TWO -> ONE; ONE -> NONE; NONE -> TWO } }

    override fun periodic() { puncher.position = this.state.position }

    enum class State(val position: Double) {
        TWO(0.75),
        ONE(0.88),
        NONE(1.0),
    }
}