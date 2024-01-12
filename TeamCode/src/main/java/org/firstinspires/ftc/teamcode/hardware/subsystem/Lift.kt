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

    var current = 0.0
    var target = 0.0

    override fun init() {
        reset()
        defaultCommand = to(current.toInt())
    }

    override fun periodic() {
        current = abs(left.currentPosition).toDouble()

        opmode.telemetry.addData("ticks", current)
        opmode.telemetry.addData("ticks error", target - current)
    }

    override fun defaultCommandExecute() {
        val error = target - current

        val g = if (abs(error) < 10 || current < 100 || error < 0) 0.0 else kG

        val kP = when {
            target == 0.0 && current <= 30 -> zeroKP
            target <= 70 -> lowKP
            error >= 0 -> posKP
            error < 0 -> negKP

            else -> 0.001
        }

        // if (target <= 70) zeroKP else if (error >= 0) posKP else negKP

        if (current in 20.0..40.0) spatula.align().execute()

        val output = (kP * error) + g * (current / Position.HIGH.ticks).pow(1.0 / 4.0)

        directions.forEach { it.key.power = output * it.value }
    }

    fun to(ticks: Int): Command {
        return LambdaCommand()
                .setInterruptible(true)
                .setInit { preparePID(ticks) }
                .setRequirements(this)
                .setExecute { defaultCommandExecute() }
                .setFinish { false }
    }

    fun to(position: Position) = to(position.ticks)

    private fun reset() {
        motors.forEach { it.direction = REVERSE }
        motors.forEach { it.mode = STOP_AND_RESET_ENCODER }
        motors.forEach { it.mode = RUN_WITHOUT_ENCODER }
        motors.forEach { it.zeroPowerBehavior = BRAKE }
    }

    fun preparePID(targetPosition: Int) {
        this.target = targetPosition.toDouble()
    }

    override fun close() {}

    enum class Position(val ticks: Int) {
        ZERO(0),
        LOW(245),
        MID(485),
        HIGH(970) // -10
    }

    companion object {
        private const val posKP = 0.0018
        private const val negKP = 0.0023
        private const val lowKP = 0.0032
        private const val zeroKP = 0.0045

        private const val kG = 0.3
    }
}