package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.util.ElapsedTime
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.Command
import org.mercurialftc.mercurialftc.scheduler.subsystems.SubsystemInterface

class WaitCommand(val milliseconds: Long) : Command {
    val timer = ElapsedTime()

    override fun initialise() = Unit

    override fun execute() = timer.reset()

    override fun end(p0: Boolean) = Unit

    override fun finished() = timer.milliseconds() >= milliseconds

    override fun getRequiredSubsystems() = setOf<SubsystemInterface>()

    override fun getRunStates() = setOf(OpModeEX.OpModeEXRunStates.INIT_LOOP)
}