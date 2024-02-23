package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift

import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.Robot.LiftHardware.motors
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.EfficientSubsystem
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class Lift(val telemetry: Telemetry) : EfficientSubsystem() {
    var actual = 0.0
    var ideal = 0.0
    var position = 0
    var target = 0

    val atZero
        get() = position == 0

    val targetIsZero
        get() = target == 0

    val cleared
        get() = position >= Position.CLEAR.ticks

    val intakeCleared
        get() = position >= Position.INTAKE.ticks

    init { reset() }

    override fun read() {
        position = abs(Robot.LiftHardware.left.currentPosition)
    }

    override fun write() {
        motors.forEach { it.targetPosition = target }

        if (abs(actual - ideal) > 0.1) {
            actual = ideal
            motors.forEach { it.power = actual }
        }

        motors.forEach { it.mode = DcMotor.RunMode.RUN_TO_POSITION }
    }

    override fun reset() {
        motors.forEach { it.targetPositionTolerance = 1 }
        motors.forEach { it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER }
        motors.forEach { it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE }
        motors.forEach { it.targetPosition = 0 }
        motors.forEach { it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER }
        motors.forEach { it.mode = DcMotor.RunMode.RUN_TO_POSITION }
    }

    fun targetInches(height: Double) = target(height * TICKS_PER_INCH * sqrt(2.0))

    fun adjust(ticks: Int) { target = max(0, min(target + ticks, Position.HIGH.ticks)) }
    fun forcefulAdjust(ticks: Int) { target += ticks }

    fun target(ticks: Double) = target(ticks.toInt())
    fun target(ticks: Int) { target = ticks; go() }

    fun stop() { ideal = 0.0 }
    fun go() { ideal = 0.7 }

    override fun periodic() {
        telemetry.addData("lift target", target)
        telemetry.addData("lift pos", position)
        telemetry.addData("lift ideal", ideal)
        telemetry.addData("lift actual", actual)
    }

    enum class Position(val ticks: Int) {
        ZERO(0),
        INTAKE(125),
        CLEAR(350),
        LOW(400),
        MID(1100),
        HIGH(1600)
    }

    companion object {
        const val TICKS_PER_INCH = 1.0
    }
}