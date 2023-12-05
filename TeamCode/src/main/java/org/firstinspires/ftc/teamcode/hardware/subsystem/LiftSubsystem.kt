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
import kotlin.math.sign

class LiftSubsystem(val opmode: OpModeEX) : Subsystem(opmode) {
    private val hw = opmode.hardwareMap

    private val right by lazy { hw["lift.right"] as DcMotorEx }
    private val left by lazy { hw["lift.left"] as DcMotorEx }

    private val directions by lazy { mapOf(right to 1, left to -1) }
    private val motors
        get() = directions.keys

    var current = 0.0
    var target = 0.0

    override fun init() {
        reset()
//        motors.forEach { it.targetPosition = target.toInt() }
        defaultCommand = to(current.toInt())
    }

    override fun periodic() {
        current = abs(left.currentPosition).toDouble()
        opModeEX.telemetry.addData("lift", current)

//        motors.forEach { if (!it.isBusy) it.power = 0.0 }
    }

    override fun defaultCommandExecute() {
        val error = target - current

        val g = if (error < 0 || current < 30) 0.0 else kG

        val output = (kP * error) + g + (kM * current)

        opmode.telemetry.addData("error", error)
        opmode.telemetry.addData("output", output)

//        motors.forEach { it.targetPosition = target.toInt() }
//        motors.forEach { it.power = 0.005 }

        directions.forEach { it.key.power = output * it.value }
    }

    fun to(ticks: Int): Command {
        opmode.telemetry.addData("ticks", ticks)
        return LambdaCommand()
                .setInterruptible(true)
                .setInit { preparePID(ticks) }
                .setRequirements(this)
                .setExecute {
//                    motors.forEach { it.targetPosition = ticks }
                    defaultCommandExecute()
                } // uses the default command execute
                .setFinish { false }
    }

    fun to(position: Position): Command {
        return to(position.ticks)
    }

    private fun reset() {
//        right.direction = REVERSE
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
        LOW(100),
        MID(200),
        HIGH(400)
    }

    companion object {
        private const val kP = 0.003
        private const val kG = 0.2
        private const val kM = 0.0005
    }
}