package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.abs

class Lift(val hw: HardwareMap, val telemetry: Telemetry) : SubsystemBase() {
    val right by lazy { hw["leftLift"] as DcMotorEx }
    val left by lazy { hw["rightLift"] as DcMotorEx }

    val motors = listOf(left, right)

    var current = 0
    var target = 0

    init { reset() }

    fun reset() {
        motors.forEach { it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER }
        motors.forEach { it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE }
        motors.forEach { it.targetPosition = 0 }
        motors.forEach { it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER }
        motors.forEach { it.mode = DcMotor.RunMode.RUN_TO_POSITION }
    }

    fun target(ticks: Int) { this.target = ticks }

    fun stop() { motors.forEach { it.power = 0.0 } }

    fun go() {
        motors.forEach { it.targetPosition = this.target }
        motors.forEach { it.power = 0.7 }
        motors.forEach { it.mode = DcMotor.RunMode.RUN_TO_POSITION }
    }

    override fun periodic() {
        telemetry.addData("lift pos", current)
        telemetry.addData("target pos", target)

        current = abs(left.currentPosition)
        go()
    }

    enum class Position(val ticks: Int) {
        ZERO(0),
        LOW(245),
        MID(485),
        HIGH(970) // -10
    }
}