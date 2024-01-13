package org.firstinspires.ftc.teamcode.manual

import com.arcrobotics.ftclib.command.CommandOpMode
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
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift.Position.*
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.TargetGoCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.PuncherNextCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.PuncherDropCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.Spatula
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.ToCommand

@TeleOp(group = "!")
class DriverControlled : CommandOpMode() {
    val lift by lazy { Lift(hardwareMap, telemetry) }
    val puncher by lazy { Puncher(hardwareMap, telemetry) }
    val spatula by lazy { Spatula(hardwareMap, telemetry) }
    val intake by lazy { Intake(hardwareMap) }
    val drive by lazy { Drive(hardwareMap) }

    val gamepad by lazy { GamepadEx(gamepad1) }

    fun target(position: Lift.Position) =
          if (position == ZERO) SequentialCommandGroup(
             ToCommand(Spatula.State.DOWN, spatula),
             WaitCommand(200),
             TargetGoCommand(position, lift),
          )
         else SequentialCommandGroup(
             ParallelCommandGroup(
                 TargetGoCommand(position, lift),
                 SequentialCommandGroup(
                     WaitCommand(100),
                     ToCommand(Spatula.State.ALIGN, spatula)
                 )
             ),

             WaitCommand(100),

                  SequentialCommandGroup(WaitCommand(200), ToCommand(Spatula.State.UP, spatula))
         )

    override fun initialize() {
        GamepadButton(gamepad, Button.X).whenPressed(target(LOW))
        GamepadButton(gamepad, Button.Y).whenPressed(target(MID))
        GamepadButton(gamepad, Button.B).whenPressed(target(HIGH))
        GamepadButton(gamepad, Button.A).whenPressed(target(ZERO))

        GamepadButton(gamepad, Button.DPAD_UP).whenPressed(IntakeToCommand(Intake.UP, intake))
        GamepadButton(gamepad, Button.DPAD_DOWN).whenPressed(IntakeToCommand(Intake.DOWN, intake))

        GamepadButton(gamepad, Button.DPAD_LEFT).whenPressed(ToCommand(Spatula.State.DOWN, spatula))
        GamepadButton(gamepad, Button.DPAD_RIGHT
        ).whenPressed(ToCommand(Spatula.State.UP, spatula))

        GamepadButton(gamepad, Button.START).whenPressed(ResetYawCommand(drive))

        GamepadButton(gamepad, Button.RIGHT_BUMPER).whenPressed(IntakeNextCommand(intake))
        GamepadButton(gamepad, Button.LEFT_BUMPER).whenPressed(PuncherNextCommand(puncher))

        Trigger { TriggerReader(gamepad, GamepadKeys.Trigger.RIGHT_TRIGGER).isDown }
                .whileActiveContinuous(ParallelCommandGroup(ForwardCommand(intake), PuncherDropCommand(puncher)))
                .whenInactive(IntakeStopCommand(intake))

        Trigger { TriggerReader(gamepad, GamepadKeys.Trigger.LEFT_TRIGGER).isDown }
                .whileActiveContinuous(ParallelCommandGroup(ReverseCommand(intake), PuncherDropCommand(puncher)))
                .whenInactive(IntakeStopCommand(intake))

        register(lift)
    }

    override fun run() {
        super.run()

        lift.periodic()

        drive.move(-gamepad.leftX, -gamepad.leftY, -gamepad.rightX)

        telemetry.update()
    }
}