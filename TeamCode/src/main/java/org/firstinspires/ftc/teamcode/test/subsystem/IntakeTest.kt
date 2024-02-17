package org.firstinspires.ftc.teamcode.test.subsystem

import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.DropIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake

@TeleOp(group = "!")
class IntakeTest : CommandOpMode() {
    val gamepad by lazy { GamepadEx(gamepad1) }
    val intake by lazy { Intake(hardwareMap) }

    override fun initialize() {
        GamepadButton(gamepad, GamepadKeys.Button.DPAD_DOWN).whenPressed(DropIntake(intake))
        GamepadButton(gamepad, GamepadKeys.Button.DPAD_UP).whenPressed(RaiseIntake(intake))
        GamepadButton(gamepad, GamepadKeys.Button.X).whenPressed(InstantCommand({ intake.target(intake.angleForHeight(0.25 * 1)) }))
        GamepadButton(gamepad, GamepadKeys.Button.Y).whenPressed(InstantCommand({ intake.target(intake.angleForHeight(0.25 * 2)) }))
        GamepadButton(gamepad, GamepadKeys.Button.A).whenPressed(InstantCommand({ intake.target(intake.angleForHeight(0.25 * 3)) }))
        GamepadButton(gamepad, GamepadKeys.Button.B).whenPressed(InstantCommand({ intake.target(intake.angleForHeight(0.25 * 4)) }))

        register(intake)
    }

    override fun run() {
        super.run()

        telemetry.addData("state", intake.target)
        telemetry.addData("angle", intake.angleForHeight(0.25))
        telemetry.update()
    }
}