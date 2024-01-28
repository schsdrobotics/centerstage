package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import kotlin.math.round

class Spatula(val hw: HardwareMap, val telemetry: Telemetry, val lift: Lift, var state: State = State.TRANSFER) : SubsystemBase() {
    val spatula by lazy {
        val it = hw["spatula"] as Servo
        it.direction = Servo.Direction.REVERSE
        it
    }

    private val encoder by lazy { hw["spatula encoder"] as AnalogInput }

    val angle get() = round(encoder.voltage / 3.3 * 360.0)
    val up get() = angle >= State.SCORE.angle
    val down get() = angle <= State.TRANSFER.angle

    fun to(state: State) { this.state = state }

    override fun periodic() {
        telemetry.addData("spatula state", state.toString())
        telemetry.addData("spatula angle", angle.toString())

        spatula.position = when (state) {
            State.SCORE -> if (true) State.SCORE.position else State.ALIGN.position
            else -> state.position
        }
    }

    enum class State(val position: Double, val angle: Double) {
        SCORE(0.55, 205.0),
        TRANSFER(0.195, 85.0),
        ALIGN(0.18, 80.0),
        HOUSE(0.17, 60.0),
        AUTO(0.3, 115.0),
    }
}