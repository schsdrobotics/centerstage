package org.firstinspires.ftc.teamcode.autonomous.opmodes.preloads

import com.arcrobotics.ftclib.command.SequentialCommandGroup
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousPosition
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand

open class PreloadsBase(side: AutonomousSide, position: AutonomousPosition) : AutonomousOpMode(side, position) {
    override fun actions() = SequentialCommandGroup(
        ActionCommand(path.purple),
        ActionCommand(path.yellow)
    )
}