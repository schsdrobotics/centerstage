package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry

class Spatula(val hw: HardwareMap, val telemetry: Telemetry) : SubsystemBase() {
    private val spatula by lazy { hw["spatula"] as Servo }

    var state = State.DOWN

    fun to(state: State) { this.state = state }

    override fun periodic() {
        spatula.position = state.position

        telemetry.addData("spatula state", state.toString())
    }

    enum class State(val position: Double) {
        UP(0.4), // 0.142
        ALIGN(0.78), // 0.142
        DOWN(0.75),
    }
}