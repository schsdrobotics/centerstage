package org.firstinspires.ftc.teamcode.manual

import com.acmerobotics.roadrunner.now
import com.arcrobotics.ftclib.command.Command
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.command.button.Trigger
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.arcrobotics.ftclib.gamepad.GamepadKeys.Button
import com.arcrobotics.ftclib.gamepad.TriggerReader
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive.Drive
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive.ResetYawCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.ForwardCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.IntakeNextCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.IntakeStopCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.IntakeToCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.ReverseCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher.LaunchCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher.Launcher
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.AdjustCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift.Position.*
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.TargetCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.TargetGoCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.PuncherNextCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.PuncherDropCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.Spatula
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.ToCommand
import kotlin.math.max

@TeleOp(group = "!")
class DriverControlled : CommandOpMode() {
    val lift by lazy { Lift(hardwareMap, telemetry) }
    val puncher by lazy { Puncher(hardwareMap, telemetry) }
    val spatula by lazy { Spatula(hardwareMap, telemetry) }
    val intake by lazy { Intake(hardwareMap) }
    val drive by lazy { Drive(hardwareMap, telemetry) }
    val launcher by lazy { Launcher(hardwareMap) }

    val gamepad by lazy { GamepadEx(gamepad1) }
    val secondary by lazy { GamepadEx(gamepad2) }

    fun target(target: Lift.Position) =
        when {
            lift.atZero && target != ZERO -> {
                SequentialCommandGroup(
                    ToCommand(Spatula.State.ALIGN, spatula),
                    TargetGoCommand(target, lift),
                    ToCommand(Spatula.State.SCORE, spatula),
                )
            }

            target == ZERO -> {
                SequentialCommandGroup(
                        TargetGoCommand(max(300, lift.target), lift),
                        ToCommand(Spatula.State.ALIGN, spatula),
                        WaitCommand(300),
                        TargetGoCommand(target, lift),
                        ToCommand(Spatula.State.TRANSFER, spatula),
                )
            }

            else -> TargetGoCommand(target, lift)
        }

    fun target(target: () -> Lift.Position) = target(target())


    override fun initialize() {
        GamepadButton(gamepad, Button.A).whenPressed(target(ZERO))
        GamepadButton(gamepad, Button.X).whenPressed(target(LOW))
        GamepadButton(gamepad, Button.Y).whenPressed(target(MID))
        GamepadButton(gamepad, Button.B).whenPressed(target(HIGH))

        GamepadButton(gamepad, Button.DPAD_UP).whenPressed(IntakeToCommand(Intake.UP, intake))
        GamepadButton(gamepad, Button.DPAD_DOWN).whenPressed(IntakeToCommand(Intake.DOWN, intake))

        GamepadButton(gamepad, Button.DPAD_LEFT).whenPressed(AdjustCommand(-50, lift))
        GamepadButton(gamepad, Button.DPAD_RIGHT).whenPressed(AdjustCommand(50, lift))

        GamepadButton(gamepad, Button.LEFT_STICK_BUTTON).whenPressed(ResetYawCommand(drive))

        GamepadButton(gamepad, Button.RIGHT_BUMPER).whenPressed(IntakeNextCommand(intake))
        GamepadButton(gamepad, Button.LEFT_BUMPER).whenPressed(PuncherNextCommand(puncher))

        GamepadButton(secondary, Button.LEFT_BUMPER).and(GamepadButton(secondary, Button.RIGHT_BUMPER)).whenActive(LaunchCommand(launcher))

        Trigger { TriggerReader(gamepad, GamepadKeys.Trigger.RIGHT_TRIGGER).isDown }
                .whileActiveContinuous(ParallelCommandGroup(ForwardCommand(intake), PuncherDropCommand(puncher), ToCommand(Spatula.State.ALIGN, spatula)))
                .whenInactive(ParallelCommandGroup(IntakeStopCommand(intake), ToCommand(Spatula.State.TRANSFER, spatula)))

        Trigger { TriggerReader(gamepad, GamepadKeys.Trigger.LEFT_TRIGGER).isDown }
                .whileActiveContinuous(ParallelCommandGroup(ReverseCommand(intake), PuncherDropCommand(puncher), ToCommand(Spatula.State.ALIGN, spatula)))
                .whenInactive(ParallelCommandGroup(IntakeStopCommand(intake), ToCommand(Spatula.State.TRANSFER, spatula)))

        register(lift)
        register(puncher)
        register(spatula)
        register(intake)
        register(drive)
    }

    override fun run() {
        val start = now()

        super.run()

        lift.periodic()

        drive.move(-gamepad.leftX, -gamepad.leftY, -gamepad.rightX)

        val end = now()

        val time = (end - start) / 1000.0

        telemetry.addData("loop time", time)

        telemetry.update()
    }
}