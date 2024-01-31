package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.util.SlewRateLimiter
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Lift(val hw: HardwareMap, val telemetry: Telemetry) : SubsystemBase() {
    val right by lazy { hw["rightLift"] as DcMotorEx }
    val left by lazy { hw["leftLift"] as DcMotorEx }

    val slewer = SlewRateLimiter(0.6, -0.6, 0.0)

    val motors by lazy { listOf(left, right) }

    var position = 0
    var target = 0

    val atZero
        get() = position == 0

    val targetIsZero
        get() = target == 0

    val cleared
        get() = position >= Position.CLEAR.ticks

    var count = 0
        set(value) { field = max(0, value) }

    init { reset() }

    fun reset() {
        motors.forEach { it.targetPositionTolerance = 1 }
        motors.forEach { it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER }
        motors.forEach { it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE }
        motors.forEach { it.targetPosition = 0 }
        motors.forEach { it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER }
        motors.forEach { it.mode = DcMotor.RunMode.RUN_TO_POSITION }
        slewer.reset(0.0)
    }

    fun adjust(ticks: Int) { this.target = max(0, min(this.target + ticks, Position.HIGH.ticks)) }

    fun target(ticks: Int) { this.target = ticks; }

//    fun toNext() { target = (++count).let { Position.values()[count % 4] }.ticks }
//    fun toPrev() { target = (--count).let { Position.values()[count % 4] }.ticks }
//
//    fun next() = (++count).let { Position.values()[it % 4] }
//    fun prev() = (--count).let { Position.values()[it % 4] }


    fun stop() {
        motors.forEach { it.power = 0.0 }
        slewer.reset(0.0)
    }

    fun go() {
        motors.forEach { it.targetPosition = this.target }
        motors.forEach { it.power = 0.7 }
        motors.forEach { it.mode = DcMotor.RunMode.RUN_TO_POSITION }
    }

    override fun periodic() {
        telemetry.addData("lift pos", position)
//        telemetry.addData("lift target pos", target)
//        telemetry.addData("lift cleared", cleared)
//        telemetry.addData("lift powers", motors.map { it.power }.joinToString(", ") { it.toString() })

        position = abs(left.currentPosition)

        if (abs(left.targetPosition - left.currentPosition) <= 2) slewer.reset(0.0)

        go()
    }

    enum class Position(val ticks: Int) {
        ZERO(0),
        INTAKE(50),
        CLEAR(200),
        LOW(300),
        MID(500),
        HIGH(700)
    }

}