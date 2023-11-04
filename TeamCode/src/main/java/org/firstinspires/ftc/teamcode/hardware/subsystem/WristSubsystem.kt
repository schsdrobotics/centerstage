package org.firstinspires.ftc.teamcode.hardware.subsystem

import com.qualcomm.robotcore.hardware.Servo
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem

class WristSubsystem(opmode: OpModeEX) : Subsystem(opmode) {
    private val hw = opmode.hardwareMap

    private val wrist by lazy { hw["wrist"] as Servo }

    override fun init() {
        wrist.scaleRange(DOWN, UP)

        wrist.position = 0.0

        defaultCommand = restore()
    }

    fun restore() = LambdaCommand()
            .setInterruptible(true)
            .setRequirements(this)
            .setExecute { wrist.position = 0.0 }
            .setFinish { false }

    fun deposit() = LambdaCommand()
            .setInterruptible(true)
            .setRequirements(this)
            .setExecute { wrist.position = 1.0 }
            .setFinish { false }

    override fun periodic() {
        opModeEX.telemetry.addData("position", wrist.position)
    }

    override fun defaultCommandExecute() = restore().execute()

    override fun close() {}

    companion object {
        private const val DOWN = 0.0
        private const val UP = 0.6
    }
}