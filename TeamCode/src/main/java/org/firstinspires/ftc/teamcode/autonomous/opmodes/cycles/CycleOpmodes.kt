package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousPosition
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide

@Autonomous(group = "Cycles")
class RedCloseCycles : CyclesBase(AutonomousSide.Red, AutonomousPosition.Backstage)

@Autonomous(group = "Cycles")
class RedFarCycles : CyclesBase(AutonomousSide.Red, AutonomousPosition.Stacks)

@Autonomous(group = "Cycles")
class BlueCloseCycles : CyclesBase(AutonomousSide.Blue, AutonomousPosition.Backstage)

@Autonomous(group = "Cycles")
class BlueFarCycles : CyclesBase(AutonomousSide.Blue, AutonomousPosition.Stacks)