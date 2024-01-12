package org.firstinspires.ftc.teamcode.hardware.subsystem

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

    override fun init() {
        motor.direction = REVERSE
        motor.zeroPowerBehavior = BRAKE

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
            .setRequirements(this)
            .setExecute { motor.power = 0.0 }
            .setFinish { false }

    override fun periodic() {}

    override fun defaultCommandExecute() = Unit

    override fun close() {}

    companion object {
        private const val SPEED = 0.7
        private const val SLOWED = (SPEED - (SPEED / 3.0))
    }
}