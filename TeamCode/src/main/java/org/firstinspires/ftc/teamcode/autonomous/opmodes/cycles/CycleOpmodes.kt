package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousPosition
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide
import org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.close.CloseCyclesBase
import org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.close.CloseCyclesPathBase
import org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.far.FarCyclesBase

@Autonomous(group = "Cycles")
class RedCloseCyclesPath : CloseCyclesPathBase(AutonomousSide.Red, AutonomousPosition.Backstage)

@Autonomous(group = "Cycles")
class RedCloseCycles : CloseCyclesBase(AutonomousSide.Red, AutonomousPosition.Backstage)

@Autonomous(group = "Cycles")
class RedFarCycles : FarCyclesBase(AutonomousSide.Red, AutonomousPosition.Stacks)

@Autonomous(group = "Cycles")
class BlueCloseCycles : CloseCyclesBase(AutonomousSide.Blue, AutonomousPosition.Backstage)

@Autonomous(group = "Cycles")
class BlueFarCycles : FarCyclesBase(AutonomousSide.Blue, AutonomousPosition.Stacks)