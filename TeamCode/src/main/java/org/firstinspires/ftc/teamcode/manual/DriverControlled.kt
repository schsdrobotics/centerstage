package org.firstinspires.ftc.teamcode.manual

import com.acmerobotics.roadrunner.now
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.CommandScheduler
import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.command.button.Trigger
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.arcrobotics.ftclib.gamepad.GamepadKeys.Button
import com.arcrobotics.ftclib.gamepad.TriggerReader
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.lynx.commands.standard.LynxSetModuleLEDColorCommand
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.cycles.LiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive.Drive
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive.ResetYawCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.IntakeIn
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.IntakeNextCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.StopIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.IntakeOut
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher.LaunchCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher.Launcher
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.led.Led
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.AdjustCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift.Position.*
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.TargetGoCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.PuncherNextCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.PuncherDropCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.Spatula
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.FlipToCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.SpatulaAdjustCommand
import org.firstinspires.ftc.teamcode.util.extensions.currentDraw

@TeleOp(group = "!")
class DriverControlled : CommandOpMode() {
    val puncher by lazy { Puncher(hardwareMap, telemetry, spatula) }
    val spatula by lazy { Spatula(hardwareMap, telemetry, lift) }
    val drive by lazy { Drive(hardwareMap, telemetry, gamepad) }
    val lift by lazy { Lift(hardwareMap, telemetry) }
    val launcher by lazy { Launcher(hardwareMap) }
    val intake by lazy { Intake(hardwareMap) }
    val led by lazy { Led(hardwareMap) }

    val secondary by lazy { GamepadEx(gamepad2) }
    val gamepad by lazy { GamepadEx(gamepad1) }

    override fun initialize() {
        GamepadButton(gamepad, Button.A).whenPressed(LiftTo(ZERO, lift, spatula))
        GamepadButton(gamepad, Button.X).whenPressed(LiftTo(LOW, lift, spatula))
        GamepadButton(gamepad, Button.Y).whenPressed(LiftTo(MID, lift, spatula))
        GamepadButton(gamepad, Button.B).whenPressed(LiftTo(HIGH, lift, spatula))

        GamepadButton(gamepad, Button.DPAD_UP).whenPressed(SpatulaAdjustCommand(0.005, spatula))
        GamepadButton(gamepad, Button.DPAD_DOWN).whenPressed(SpatulaAdjustCommand(-0.005, spatula))

        GamepadButton(gamepad, Button.DPAD_LEFT).whenPressed(AdjustCommand(-50, lift))
        GamepadButton(gamepad, Button.DPAD_RIGHT).whenPressed(AdjustCommand(50, lift))

        GamepadButton(gamepad, Button.LEFT_STICK_BUTTON).whenPressed(ResetYawCommand(drive))

        GamepadButton(gamepad, Button.RIGHT_BUMPER).whenPressed(IntakeNextCommand(intake))

        GamepadButton(gamepad, Button.LEFT_BUMPER).whenPressed(PuncherNextCommand(puncher))
        GamepadButton(gamepad, Button.DPAD_LEFT).whenPressed(FlipToCommand(Spatula.State.SCORE, spatula))
        GamepadButton(gamepad, Button.DPAD_RIGHT).whenPressed(FlipToCommand(Spatula.State.ALIGN, spatula))

        GamepadButton(secondary, Button.LEFT_BUMPER).and(GamepadButton(secondary, Button.RIGHT_BUMPER)).whenActive(LaunchCommand(launcher))

        Trigger { TriggerReader(gamepad, GamepadKeys.Trigger.RIGHT_TRIGGER).isDown }
                .whileActiveContinuous(
                        if (spatula.state != Spatula.State.SCORE) {
                            ParallelCommandGroup(
                                    IntakeIn(intake) { gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) },
                                    PuncherDropCommand(puncher),
                                    TargetGoCommand(25, lift)
                            )
                        } else InstantCommand()
                )
                .whenInactive(
                        ParallelCommandGroup(
                                StopIntake(intake),
                                FlipToCommand(Spatula.State.TRANSFER, spatula),
                                TargetGoCommand(0, lift)
                        )
                )

        Trigger { TriggerReader(gamepad, GamepadKeys.Trigger.LEFT_TRIGGER).isDown }
                .whileActiveContinuous(
                        if (spatula.state != Spatula.State.SCORE) {
                            ParallelCommandGroup(
                                    IntakeOut(intake) { gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) },
                                    PuncherDropCommand(puncher),
                                    TargetGoCommand(25, lift)
                            )
                        } else InstantCommand()
                )
                .whenInactive(
                        ParallelCommandGroup(
                                StopIntake(intake),
                                FlipToCommand(Spatula.State.TRANSFER, spatula),
                                TargetGoCommand(0, lift)
                        )
                )

        register(led)

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
        telemetry.addData("adjustment", spatula.adjustment)
        telemetry.addData("intake state", intake.target)
        telemetry.update()
    }
}