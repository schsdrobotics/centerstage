package org.firstinspires.ftc.teamcode.test.motor

import com.acmerobotics.roadrunner.now
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.lynx.commands.standard.LynxSetModuleLEDColorCommand
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive.Drive
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.AdjustCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.util.extensions.currentDraw

@TeleOp
class ManualLiftAdjustment : CommandOpMode() {
    val drive by lazy { Drive(hardwareMap, telemetry, gamepad) }
    val lift by lazy { Lift(hardwareMap, telemetry) }

    val gamepad by lazy { GamepadEx(gamepad1) }

    override fun initialize() {
        lift.motors.forEach { it.power = 0.5 }

        GamepadButton(gamepad, GamepadKeys.Button.DPAD_LEFT).whenPressed(AdjustCommand(-50, lift))
        GamepadButton(gamepad, GamepadKeys.Button.DPAD_RIGHT).whenPressed(AdjustCommand(50, lift))

        hardwareMap.getAll(LynxModule::class.java).forEach { it.sendCommand(LynxSetModuleLEDColorCommand(it, 155.toByte(), 0, 155.toByte())) }
    }

    override fun run() {
        val start = now()

        drive.periodic()

        super.run()

        val end = now()
        val time = end - start


        telemetry.addData("loop time (hZ)", 1.0 / time)
        telemetry.addData("current", currentDraw)
        telemetry.update()
    }
}