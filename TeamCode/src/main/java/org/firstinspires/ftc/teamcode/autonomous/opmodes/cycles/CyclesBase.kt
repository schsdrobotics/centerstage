package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles

import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
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
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.FlipDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.TransferDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.DropIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeIn
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeToStackHeight
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.StopIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropPixels
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.PunchPixels

open class CyclesBase(side: AutonomousSide, position: AutonomousPosition) : AutonomousOpMode(side, position) {
    override fun first() { intake.target = DOWN_ANGLE; intake.periodic() }

    override fun actions() = SequentialCommandGroup(
            DropIntake(intake),
            ActionCommand(path.purple),
            RaiseIntake(intake),

            ActionCommand(path.yellow),
            DropPixels(puncher),

            ActionCommand(path.cycles.initial.backstage),

            IntakeToStackHeight(4.0, intake),

            SequentialCommandGroup(
                    MoveLiftTo(Lift.Position.INTAKE.ticks + 25, lift),
                    IntakeIn(intake) { 1.0 },
                    WaitCommand(750)
            ),

            ParallelCommandGroup(
                 ActionCommand(path.cycles.initial.stacks),
                 SequentialCommandGroup(
                     WaitCommand(500),
                     StopIntake(intake),
                     TransferDeposit(deposit),
                     ParallelCommandGroup(
                         MoveLiftTo(Lift.Position.ZERO, lift),
                         RaiseIntake(intake),
                         PunchPixels(puncher)
                     )
                 ),
            ),

            LiftTo(Lift.Position.LOW, lift, deposit),
            FlipDeposit(90.0, deposit)
    )
}