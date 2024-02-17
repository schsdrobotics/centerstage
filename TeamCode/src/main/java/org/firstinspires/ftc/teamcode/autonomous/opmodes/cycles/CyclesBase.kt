package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles

import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousPosition
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.DropIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeIn
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.StopIntake

open class CyclesBase(side: AutonomousSide, position: AutonomousPosition) : AutonomousOpMode(side, position) {
    override fun first() { intake.target = Intake.DOWN_ANGLE; intake.periodic() }

    override fun actions() = SequentialCommandGroup(
            DropIntake(intake),
            ActionCommand(path.purple),
            RaiseIntake(intake),

            ActionCommand(path.yellow),

            ActionCommand(path.cycles.initial.backstage),

            DropIntake(intake),

            SequentialCommandGroup(
                    IntakeIn(intake) { 1.0 },
                    WaitCommand(1000)
            ),

            ParallelCommandGroup(
                    ActionCommand(path.cycles.initial.stacks),
                    SequentialCommandGroup(
                        WaitCommand(1500),
                        StopIntake(intake),
                        RaiseIntake(intake),
                    )
            )
    )
}