package org.firstinspires.ftc.teamcode.test.subsystem

import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.Spatula
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.FlipToCommand

@TeleOp(group = "!")
class SpatulaTest : CommandOpMode() {
    val lift by lazy { Lift(hardwareMap, telemetry) }
    val spatula by lazy { Spatula(hardwareMap, telemetry, lift) }

    val gamepad by lazy { GamepadEx(gamepad1) }

    override fun initialize() {
        GamepadButton(gamepad, GamepadKeys.Button.DPAD_UP).whenPressed(FlipToCommand(Spatula.State.SCORE, spatula))
        GamepadButton(gamepad, GamepadKeys.Button.DPAD_DOWN).whenPressed(FlipToCommand(Spatula.State.ALIGN, spatula))
    }

    override fun run() {
        super.run()

        telemetry.addData("state", spatula.state)
        telemetry.addData("angle", spatula.angle)
        telemetry.update()
    }
}