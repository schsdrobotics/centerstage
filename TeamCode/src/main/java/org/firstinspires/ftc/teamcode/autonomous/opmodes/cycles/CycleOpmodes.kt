package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousPosition
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide

@Autonomous(group = "Preloads")
class RedCloseCycles : CyclesBase(AutonomousSide.Red, AutonomousPosition.Backstage)

@Autonomous(group = "Preloads")
class RedFarCycles : CyclesBase(AutonomousSide.Red, AutonomousPosition.Stacks)

@Autonomous(group = "Preloads")
class BlueCloseCycles : CyclesBase(AutonomousSide.Blue, AutonomousPosition.Backstage)

@Autonomous(group = "Preloads")
class BlueFarCycles : CyclesBase(AutonomousSide.Blue, AutonomousPosition.Stacks)