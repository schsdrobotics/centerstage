package org.firstinspires.ftc.teamcode.hardware.subsystem

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem


class Intake(opmode: OpModeEX) : Subsystem(opmode) {
    private val hw = opmode.hardwareMap

    private val motor by lazy { hw["intake"] as DcMotor }

    override fun init() {
        motor.direction = REVERSE
        motor.zeroPowerBehavior = BRAKE

//        defaultCommand = stop()
    }

    fun reverse() = LambdaCommand()
            .setRequirements(this)
            .setExecute { motor.power = -(SPEED - (SPEED / 3.0)) }
            .setFinish { false }

    fun spin() = LambdaCommand()
            .setRequirements(this)
            .setExecute { motor.power = SPEED }
            .setFinish { false }

    fun fast() = LambdaCommand()
            .setRequirements(this)
            .setExecute { motor.power = 1.0 }
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
    }
}