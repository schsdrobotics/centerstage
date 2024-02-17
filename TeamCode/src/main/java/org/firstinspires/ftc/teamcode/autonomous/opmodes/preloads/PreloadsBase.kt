package org.firstinspires.ftc.teamcode.autonomous.opmodes.preloads

import com.arcrobotics.ftclib.command.SequentialCommandGroup
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousPosition
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide
import org.firstinspires.ftc.teamcode.hardware.cycles.LiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.DropIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropPixels

open class PreloadsBase(side: AutonomousSide, position: AutonomousPosition) : AutonomousOpMode(side, position) {
    override fun first() { intake.target = Intake.DOWN_ANGLE; intake.periodic() }

    override fun actions() = SequentialCommandGroup(
        DropIntake(intake),
        ActionCommand(path.purple),
        RaiseIntake(intake),

        ActionCommand(path.yellow),
        LiftTo(Lift.Position.LOW, lift, deposit),
        DropPixels(puncher)
    )
}