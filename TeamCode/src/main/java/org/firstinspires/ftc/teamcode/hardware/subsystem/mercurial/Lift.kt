package org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial

import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.*
import com.qualcomm.robotcore.hardware.DcMotor.RunMode.*
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.util.sugar.WaitCommand
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.Command
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.commands.SequentialCommandGroup
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Lift(val opmode: OpModeEX) : Subsystem(opmode) {
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
//        spatula.liftAt(current)

        opmode.telemetry.addData("ticks", current)
        opmode.telemetry.addData("ticks error", target - current)
    }

    override fun defaultCommandExecute() {
        motors.forEach { it.targetPosition = this.target }
        motors.forEach { it.power = 0.5 }
        motors.forEach { it.mode = RUN_TO_POSITION }
    }

    fun next(): Command {
        val position = Position.values().map { Pair(it, it.ticks - current) }.maxByOrNull { it.second }!!.first

        return to(position)
    }

    fun previous(): Command {
        val position = Position.values().map { Pair(it, it.ticks - current) }.minByOrNull { it.second }!!.first

        return to(position)
    }

    fun adjust(offset: Int) = to(max(Position.ZERO.ticks, min(Position.HIGH.ticks, current + offset)))

    fun to(ticks: Int): Command {
        return LambdaCommand()
                .setInterruptible(true)
                .setRequirements(this)
                .setInit { this.target = ticks }
                .setExecute { defaultCommandExecute() }
                .setFinish { (abs(target - current) <= 3) && false }
    }

    fun bang() = SequentialCommandGroup().addCommands(to(30), WaitCommand(200), to(0))

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