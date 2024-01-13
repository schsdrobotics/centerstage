package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift

import com.arcrobotics.ftclib.command.CommandBase

class GoCommand(private val lift: Lift) : CommandBase() {
    init { addRequirements(lift) }

    override fun execute() { lift.go() }

    override fun isFinished() = !lift.left.isBusy
}