package org.firstinspires.ftc.teamcode.autonomous.opmodes.preloads

import com.arcrobotics.ftclib.command.SequentialCommandGroup
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
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropPixels

open class PreloadsBase(side: AutonomousSide, position: AutonomousPosition) : AutonomousOpMode(side, position) {
    override fun first() { intake.target = DOWN_ANGLE; intake.periodic() }

    override fun actions() = SequentialCommandGroup(
        DropIntake(intake),
        ActionCommand(path.purple),
        RaiseIntake(intake),

        ActionCommand(path.yellow),
        LiftTo(Lift.Position.LOW, lift, deposit),
        DropPixels(puncher)
    )
}