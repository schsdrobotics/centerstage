package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.far

import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Vector2d
import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousPosition
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.cycles.LiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive.DriveAdjustCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.DropIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeIn
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeToStackHeight
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.StopIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.PunchPixels
import org.firstinspires.ftc.teamcode.util.extensions.currentDraw

open class FarCyclesBase(side: AutonomousSide, position: AutonomousPosition) : AutonomousOpMode(side, position) {
	override fun first() { Robot.intake.target = Robot.IntakeHardware.Configuration.DOWN_ANGLE; Robot.intake.periodic() }

	override fun actions() = SequentialCommandGroup(
		DropIntake(Robot.intake),
		ActionCommand(path.purple),
		RaiseIntake(Robot.intake),

		ActionCommand(path.cycles.initial.stacks),

		IntakeToStackHeight(4.0, Robot.intake),

		SequentialCommandGroup(
			MoveLiftTo(Lift.Position.INTAKE.ticks + 25, Robot.lift),
			IntakeIn(Robot.intake) { 1.0 },
			WaitCommand(750)
		),

		ParallelCommandGroup(
			ActionCommand(path.cycles.initial.backstage),
			SequentialCommandGroup(
				WaitCommand(500),
				StopIntake(Robot.intake),
				ParallelCommandGroup(
					RaiseIntake(Robot.intake),
					PunchPixels(Robot.puncher)
				)
			),
		),

		LiftTo(Lift.Position.MID, Robot.lift, Robot.deposit),

		DriveAdjustCommand(Vector2d(0.0, -0.1), 0.0, { currentDraw > 2.5 }, drive),

		InstantCommand({ drive.setDrivePowers(PoseVelocity2d(Vector2d(0.0, 0.0), 0.0)) })
	)
}