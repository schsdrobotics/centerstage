package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class Lift(val hw: HardwareMap, val telemetry: Telemetry) : SubsystemBase() {
    val right by lazy { hw["rightLift"] as DcMotorEx }
    val left by lazy { hw["leftLift"] as DcMotorEx }

    val motors by lazy { listOf(left, right) }

    var position = 0
    var target = 0

    val atZero
        get() = position == 0

    val targetIsZero
        get() = target == 0

    val cleared
        get() = position >= Position.CLEAR.ticks

    init { reset() }

    fun reset() {
        motors.forEach { it.targetPositionTolerance = 1 }
        motors.forEach { it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER }
        motors.forEach { it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE }
        motors.forEach { it.targetPosition = 0 }
        motors.forEach { it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER }
        motors.forEach { it.mode = DcMotor.RunMode.RUN_TO_POSITION }
    }

    fun targetInches(height: Double) = target(height * TICKS_PER_INCH * sqrt(2.0))

    fun adjust(ticks: Int) { this.target = max(0, min(this.target + ticks, Position.HIGH.ticks)) }

    fun target(ticks: Int) { this.target = ticks; }
    fun target(ticks: Double) = target(ticks.toInt())

    fun stop() {
        motors.forEach { it.power = 0.0 }
    }

    fun go() {
        motors.forEach { it.targetPosition = this.target }
        motors.forEach { it.power = 0.7 }
        motors.forEach { it.mode = DcMotor.RunMode.RUN_TO_POSITION }
    }

    override fun periodic() {
        telemetry.addData("lift pos", position)

        position = abs(left.currentPosition)

        go()
    }

    enum class Position(val ticks: Int) {
        ZERO(0),
        INTAKE(125),
        CLEAR((200 * 2.5).toInt()),
        LOW((300 * 2.5).toInt()),
        MID((500 * 2.5).toInt()),
        HIGH((700 * 2.5).toInt())
    }

    companion object {
        const val TICKS_PER_INCH = 1.0
    }
}