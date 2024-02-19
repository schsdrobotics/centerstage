package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift

class GoCommand(private val lift: Lift) : CommandBase() {
    init { addRequirements(lift) }

    override fun execute() { lift.go() }

    override fun isFinished() = !Robot.LiftHardware.left.isBusy
}