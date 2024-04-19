package org.firstinspires.ftc.teamcode.autonomous.opmodes.preloads.bases

import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import com.arcrobotics.ftclib.command.WaitUntilCommand
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.Nature
import org.firstinspires.ftc.teamcode.autonomous.framework.Side
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.Robot.deposit
import org.firstinspires.ftc.teamcode.hardware.Robot.intake
import org.firstinspires.ftc.teamcode.hardware.Robot.puncher
import org.firstinspires.ftc.teamcode.hardware.cycles.UnsafeLiftZero
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.ScoreDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.TransferDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeOut
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.StopIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropPixels
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.PunchPixels

open class ClosePreloadsBase(side: Alliance, position: Side) : AutonomousOpMode(side, position, Nature.Preloads) {
    override fun first() {
        intake.target = 20.0; intake.periodic()
        PunchPixels(puncher).initialize()
    }

    override fun actions() = SequentialCommandGroup(
        IntakeTo(20.0, intake),

        ActionCommand(path.purple),
        IntakeOut(intake) { 1.0 },
        WaitCommand(1000),
        StopIntake(intake),

        TransferDeposit(deposit, false),
        PunchPixels(puncher),

        MoveLiftTo(Lift.Position.LOW, Robot.lift),
        ScoreDeposit(deposit, false),
        MoveLiftTo(100, Robot.lift),

        ParallelCommandGroup(
            ActionCommand(path.yellow),
            SequentialCommandGroup(
                RaiseIntake(intake),
                WaitUntilCommand { drive.pose.position.x >= 50.0 },
                WaitCommand(1000),
                DropPixels(puncher),
            )
        ),

        ActionCommand(path.extras.first()),

        MoveLiftTo(Lift.Position.LOW.ticks, Robot.lift),
        TransferDeposit(deposit, false),
        WaitCommand(250),
        UnsafeLiftZero(Robot.lift),

        ActionCommand(path.park)
    )
}