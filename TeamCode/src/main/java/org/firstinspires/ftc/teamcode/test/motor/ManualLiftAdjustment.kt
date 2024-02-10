package org.firstinspires.ftc.teamcode.test.motor

import com.acmerobotics.roadrunner.now
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.CommandScheduler
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.lynx.commands.standard.LynxSetModuleLEDColorCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.AdjustCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.util.extensions.currentDraw

class ManualLiftAdjustment : CommandOpMode() {
    val lift by lazy { Lift(hardwareMap, telemetry) }

    val gamepad by lazy { GamepadEx(gamepad1) }

    override fun initialize() {
        GamepadButton(gamepad, GamepadKeys.Button.DPAD_LEFT).whenPressed(AdjustCommand(-50, lift))
        GamepadButton(gamepad, GamepadKeys.Button.DPAD_RIGHT).whenPressed(AdjustCommand(50, lift))

        hardwareMap.getAll(LynxModule::class.java).forEach { it.sendCommand(LynxSetModuleLEDColorCommand(it, 155.toByte(), 0, 155.toByte())) }

        while (opModeInInit()) {
            CommandScheduler.getInstance().run()
        }
    }

    override fun run() {
        val start = now()

        super.run()

        val end = now()
        val time = end - start

        telemetry.addData("loop time (hZ)", 1.0 / time)
        telemetry.addData("current", currentDraw)
        telemetry.update()
    }
}