package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry

class Spatula(val hw: HardwareMap, val telemetry: Telemetry) : SubsystemBase() {
    private val spatula by lazy {
        val it = hw["spatula"] as Servo
        it.direction = Servo.Direction.REVERSE
        it
    }

    var state = State.TRANSFER

    fun to(state: State) { this.state = state }

    override fun periodic() {
        spatula.position = state.position

        telemetry.addData("spatula state", state.toString())
    }

    enum class State(val position: Double) {
        SCORE(0.55),
        TRANSFER(0.207),
        ALIGN(0.195),
    }
}