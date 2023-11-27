package org.firstinspires.ftc.teamcode.hardware.subsystem

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.Servo
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem

class DepositSubsystem(opmode: OpModeEX) : Subsystem(opmode) {
    private val hw = opmode.hardwareMap

    private val door by lazy { hw["door"] as Servo }
    private val transfer by lazy { hw["transfer"] as CRServo }

    override fun init() { }

    fun open() = LambdaCommand()
            .setRequirements(this)
            .setExecute { door.position = OPEN }
            .setFinish { false }

    fun shut() = LambdaCommand()
            .setRequirements(this)
            .setExecute { door.position = CLOSED }
            .setFinish { false }

    fun spin() = LambdaCommand()
            .setInterruptible(false)
            .setRequirements(this)
            .setExecute { transfer.power = -1.0 }
            .setFinish { false }

    fun stop() = LambdaCommand()
            .setInterruptible(false)
            .setRequirements(this)
            .setExecute { transfer.power = 0.0 }
            .setFinish { false }

    override fun periodic() {}

    override fun defaultCommandExecute() {}

    override fun close() {}

    companion object {
        private const val CLOSED = 1.0
        private const val OPEN = 0.0
    }
}