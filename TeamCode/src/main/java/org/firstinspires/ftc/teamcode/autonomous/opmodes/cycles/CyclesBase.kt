package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles

import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Vector2d
import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import com.arcrobotics.ftclib.command.WaitUntilCommand
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousPosition
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide
import org.firstinspires.ftc.teamcode.hardware.Robot.IntakeHardware.Configuration.DOWN_ANGLE
import org.firstinspires.ftc.teamcode.hardware.Robot.deposit
import org.firstinspires.ftc.teamcode.hardware.Robot.intake
import org.firstinspires.ftc.teamcode.hardware.Robot.lift
import org.firstinspires.ftc.teamcode.hardware.Robot.puncher
import org.firstinspires.ftc.teamcode.hardware.cycles.LiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.DropIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeIn
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeToStackHeight
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.StopIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropPixels
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.PunchPixels
import org.firstinspires.ftc.teamcode.util.extensions.currentDraw

open class CyclesBase(side: AutonomousSide, position: AutonomousPosition) : AutonomousOpMode(side, position) {
	override fun first() {
		intake.target = DOWN_ANGLE; intake.periodic()
	}

	override fun actions() = SequentialCommandGroup(
		DropIntake(intake),
		ActionCommand(path.purple),
		RaiseIntake(intake),

		ActionCommand(path.yellow),
		DropPixels(puncher),

		ActionCommand(path.cycles.initial.stacks),

		IntakeToStackHeight(4.0, intake),

		SequentialCommandGroup(
			MoveLiftTo(Lift.Position.INTAKE.ticks + 25, lift),
			IntakeIn(intake) { 1.0 },
			WaitCommand(750)
		),

		ParallelCommandGroup(
			ActionCommand(path.cycles.initial.backstage),
			SequentialCommandGroup(
				WaitCommand(500),
				StopIntake(intake),
				ParallelCommandGroup(
					RaiseIntake(intake),
					PunchPixels(puncher)
				)
			),
		),

		LiftTo(Lift.Position.MID, lift, deposit),

//        // this gets the rotation of the end pose and sends commands to go
//		(Rotation2d.fromDouble(300.deg)).let {
//            val current = Vector2d(it.real, it.imag) // where i am
//            val direction = Vector2d(1.0, 0.0) // where i want to go
//			InstantCommand({ drive.setDrivePowers(PoseVelocity2d(current + direction, 0.deg)) })
//		},

        WaitUntilCommand {
			drive.setDrivePowers(PoseVelocity2d(Vector2d(25.0, 0.0), 0.0))
			currentDraw > 2.5
		},

        InstantCommand({ drive.setDrivePowers(PoseVelocity2d(Vector2d(0.0, 0.0), 0.0)) })
	)
}