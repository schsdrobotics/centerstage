package org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE
import com.qualcomm.robotcore.hardware.Servo
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem


class Intake(opmode: OpModeEX) : Subsystem(opmode) {
    private val hw = opmode.hardwareMap

    private val motor by lazy { hw["intake"] as DcMotor }

    private val leftChad by lazy { hw["leftChad"] as Servo }
    private val rightChad by lazy { hw["rightChad"] as Servo }

    private val chads by lazy { listOf(leftChad, rightChad) }

    var target = (UP + DOWN) / 2.0

    override fun init() {
        motor.direction = REVERSE
        motor.zeroPowerBehavior = BRAKE

        chads.forEach { it.direction = Servo.Direction.REVERSE }

        defaultCommand = stop()
    }

    fun reverse(slow: Boolean) = LambdaCommand()
            .setRequirements(this)
            .setExecute { motor.power = -(if (slow) SLOWED else SPEED) }
            .setFinish { false }

    fun forward(slow: Boolean) = LambdaCommand()
            .setRequirements(this)
            .setExecute { motor.power = if (slow) SLOWED else SPEED }
            .setFinish { false }

    fun stop() = LambdaCommand()
            .setInterruptible(true)
            .setRequirements(this)
            .setExecute { motor.power = 0.0 }
            .setFinish { false }

    fun to(position: Double) = LambdaCommand()
            .setRequirements(this)
            .setExecute { target = position }
            .setFinish { false }

    fun up() = to(UP)
    fun down() = to(DOWN)

    override fun periodic() { chads.forEach { it.position = target } }

    override fun defaultCommandExecute() = Unit

    override fun close() {}

    companion object {
        private const val SPEED = 1.0
        private const val SLOWED = 1.0

        private const val DOWN = 0.07
        private const val UP = 0.23

    }
}