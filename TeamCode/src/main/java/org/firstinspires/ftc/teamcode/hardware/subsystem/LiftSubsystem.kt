package org.firstinspires.ftc.teamcode.hardware.subsystem

import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.*
import com.qualcomm.robotcore.hardware.DcMotor.RunMode.*
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.*
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.Command
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem
import org.mercurialftc.mercurialftc.util.hardware.cachinghardwaredevice.CachingDcMotorEX
import kotlin.math.abs

class LiftSubsystem(val opmode: OpModeEX) : Subsystem(opmode) {
    private val hw = opmode.hardwareMap

    private val right by lazy { CachingDcMotorEX(hw["lift.right"] as DcMotorEx) }
    private val left by lazy { CachingDcMotorEX(hw["lift.left"] as DcMotorEx) }

    private val directions by lazy { mapOf(right to 1, left to -1) }
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
    }

    override fun defaultCommandExecute() {
        val kP = 0.003
        val error = target - current

        val output = kP * error

        directions.forEach { it.key.power = output * it.value }
    }

    fun to(ticks: Int): Command {
        opmode.telemetry.addData("ticks", ticks)
        return LambdaCommand()
                .setInterruptible(true)
                .setInit { preparePID(ticks) }
                .setRequirements(this)
                .setExecute { defaultCommandExecute() } // uses the default command execute
                .setFinish { false }
    }

    fun to(position: Position): Command {
        return to(position.ticks)
    }

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
        LOW(170),
        MID(340),
        HIGH(640)
    }
}