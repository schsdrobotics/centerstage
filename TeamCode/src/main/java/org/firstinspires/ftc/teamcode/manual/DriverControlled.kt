package org.firstinspires.ftc.teamcode.manual

import com.acmerobotics.roadrunner.now
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
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.led.Led
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.AdjustCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift.Position.*
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.TargetGoCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.PuncherNextCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.PuncherDropCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.Spatula
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.ToCommand
import org.firstinspires.ftc.teamcode.util.sugar.nowMs
import kotlin.math.max

@TeleOp(group = "!")
class DriverControlled : CommandOpMode() {
    val drive by lazy { Drive(hardwareMap, telemetry, gamepad) }
    val spatula by lazy { Spatula(hardwareMap, telemetry, lift) }
    val puncher by lazy { Puncher(hardwareMap, telemetry) }
    val lift by lazy { Lift(hardwareMap, telemetry) }
    val launcher by lazy { Launcher(hardwareMap) }
    val intake by lazy { Intake(hardwareMap) }
    val led by lazy { Led(hardwareMap) }

    val secondary by lazy { GamepadEx(gamepad2) }
    val gamepad by lazy { GamepadEx(gamepad1) }

    var condition = "none"

    // TODO: move into Lift
    // x, y -> sequentially, x, then y
    // x + y -> x and y in parallel
    fun to(target: Lift.Position) =
        when {
            // default case: at zero, target non-zero
            // align, go, score
            lift.atZero && target != ZERO -> {
                condition = ("lift.atZero && target != ZERO")
                SequentialCommandGroup(
                    ToCommand(Spatula.State.ALIGN, spatula),
                    TargetGoCommand(target, lift),
                    ToCommand(Spatula.State.SCORE, spatula),
                )
            }

            // adjustment case: lift not zeroed, target isn't zero
            // go + score
            lift.position >= CLEAR.ticks && target != ZERO -> {
                condition = ("lift.position >= CLEAR.ticks && target != ZERO")
                SequentialCommandGroup(
                        TargetGoCommand(target, lift),
                        ToCommand(Spatula.State.SCORE, spatula),
                )
            }

            //
            lift.position <= 30 && target == ZERO -> {
                condition = ("lift.position <= 30 && target == ZERO")

                SequentialCommandGroup(
                        ToCommand(Spatula.State.ALIGN, spatula),
                        TargetGoCommand(ZERO, lift),
                        ToCommand(Spatula.State.TRANSFER, spatula),
                )
            }

            // restorative case: target is zero
            // ensure lift is high enough before dropping
            // align, wait 300ms, (go + transfer)
            target == ZERO -> {
                condition = ("target == ZERO")

                SequentialCommandGroup(
                        if (!lift.cleared) TargetGoCommand(300, lift) else InstantCommand(),
                        ToCommand(Spatula.State.ALIGN, spatula),
                        ParallelCommandGroup(
                                TargetGoCommand(ZERO, lift),
                                ToCommand(Spatula.State.TRANSFER, spatula)
                        )
                )
            }

            // base case
            else -> {
                telemetry.addLine("else")

                SequentialCommandGroup(
                        TargetGoCommand(target, lift),
                        ToCommand(Spatula.State.SCORE, spatula),
                )
            }
        }

    fun to(target: () -> Lift.Position) = to(target())


    override fun initialize() {
        GamepadButton(gamepad, Button.X).whenPressed(InstantCommand({ to(LOW).schedule() }))
        GamepadButton(gamepad, Button.Y).whenPressed(InstantCommand({ to(MID).schedule() }))
        GamepadButton(gamepad, Button.B).whenPressed(InstantCommand({ to(HIGH).schedule() }))

        GamepadButton(gamepad, Button.DPAD_UP).whenPressed(IntakeToCommand(Intake.UP, intake))
        GamepadButton(gamepad, Button.DPAD_DOWN).whenPressed(IntakeToCommand(Intake.DOWN, intake))

        GamepadButton(gamepad, Button.DPAD_LEFT).whenPressed(AdjustCommand(-50, lift))
        GamepadButton(gamepad, Button.DPAD_RIGHT).whenPressed(AdjustCommand(50, lift))

        GamepadButton(gamepad, Button.LEFT_STICK_BUTTON).whenPressed(ResetYawCommand(drive))

        GamepadButton(gamepad, Button.RIGHT_BUMPER).whenPressed(IntakeNextCommand(intake))
        GamepadButton(gamepad, Button.LEFT_BUMPER).whenPressed(PuncherNextCommand(puncher))

        GamepadButton(secondary, Button.LEFT_BUMPER).and(GamepadButton(secondary, Button.RIGHT_BUMPER)).whenActive(LaunchCommand(launcher))


        Trigger { TriggerReader(gamepad, GamepadKeys.Trigger.RIGHT_TRIGGER).isDown }
                .whileActiveContinuous(
                    ParallelCommandGroup(
                        ForwardCommand(intake) { gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) },
                        PuncherDropCommand(puncher),
                        ToCommand(Spatula.State.HOUSE, spatula)
                    )
                )
                .whenInactive(
                    ParallelCommandGroup(
                        IntakeStopCommand(intake),
                        ToCommand(Spatula.State.TRANSFER, spatula)
                    )
                )

        Trigger { TriggerReader(gamepad, GamepadKeys.Trigger.LEFT_TRIGGER).isDown }
                .whileActiveContinuous(
                    ParallelCommandGroup(
                        ReverseCommand(intake) { gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) },
                        PuncherDropCommand(puncher),
                        ToCommand(Spatula.State.HOUSE, spatula)
                    )
                )
                .whenInactive(
                    ParallelCommandGroup(
                        IntakeStopCommand(intake),
                        ToCommand(Spatula.State.TRANSFER, spatula)
                    )
                )

        register(led)
    }

    override fun run() {
        val start = now()

        super.run()

        val end = now()
        val time = end - start

        led.periodic()

        telemetry.addData("loop time (hZ)", 1.0 / time)
        telemetry.addData("condition", condition)
        telemetry.update()
    }
}