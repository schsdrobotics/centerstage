package org.firstinspires.ftc.teamcode.hardware.subsystem

import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.*
import com.qualcomm.robotcore.hardware.DcMotor.RunMode.*
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.*
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.Command
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem
import kotlin.math.abs
import kotlin.math.pow

class Lift(val opmode: OpModeEX, val spatula: Spatula) : Subsystem(opmode) {
    private val hw = opmode.hardwareMap

    private val right by lazy { hw["leftLift"] as DcMotorEx }
    private val left by lazy { hw["rightLift"] as DcMotorEx }

    private val directions by lazy { mapOf(right to -1, left to -1) }
    private val motors
        get() = directions.keys

    var current = 0
    var target = 0

    override fun init() {
        reset()
        defaultCommand = to(current)
    }

    override fun periodic() {
        current = abs(left.currentPosition)

        opmode.telemetry.addData("ticks", current)
        opmode.telemetry.addData("ticks error", target - current)
    }

    override fun defaultCommandExecute() {
        motors.forEach { it.targetPosition = this.target }
        motors.forEach { it.power = 0.3 }
        motors.forEach { it.mode = RUN_TO_POSITION }
    }

    fun to(ticks: Int): Command {
        return LambdaCommand()
                .setInterruptible(true)
                .setInit { this.target = ticks }
                .setRequirements(this)
                .setExecute { defaultCommandExecute() }
                .setFinish { false }
    }

    fun to(position: Position) = to(position.ticks)

    private fun reset() {
        motors.forEach { it.mode = STOP_AND_RESET_ENCODER }
        motors.forEach { it.zeroPowerBehavior = BRAKE }
        motors.forEach { it.targetPosition = 0 }
        motors.forEach { it.mode = RUN_WITHOUT_ENCODER }
        motors.forEach { it.mode = RUN_TO_POSITION }
    }

    override fun close() {}

    enum class Position(val ticks: Int) {
        ZERO(0),
        LOW(245),
        MID(485),
        HIGH(970) // -10
    }
}