package org.firstinspires.ftc.teamcode.test.subsystem

import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.PuncherDropCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.PuncherNextCommand

@TeleOp(group = "!")
class PuncherTest : CommandOpMode() {
    val puncher by lazy { Puncher(hardwareMap, telemetry) }

    val gamepad by lazy { GamepadEx(gamepad1) }

    override fun initialize() {
        GamepadButton(gamepad, GamepadKeys.Button.DPAD_RIGHT).whenPressed(PuncherNextCommand(puncher))
        GamepadButton(gamepad, GamepadKeys.Button.DPAD_UP).whenPressed(PuncherDropCommand(puncher))
    }

    override fun run() {
        super.run()

        telemetry.addData("state", puncher.state)
        telemetry.addData("distance", puncher.distance)
        telemetry.update()
    }
}