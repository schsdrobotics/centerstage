package org.firstinspires.ftc.teamcode.autonomous.preloads

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.autonomous.AutonomousPosition
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide

@Autonomous(group = "Preloads")
class RedClosePreloads : Preloads(AutonomousSide.Red, AutonomousPosition.Backstage)

@Autonomous(group = "Preloads")
class RedFarPreloads : Preloads(AutonomousSide.Red, AutonomousPosition.Stacks)

@Autonomous(group = "Preloads")
class BlueClosePreloads : Preloads(AutonomousSide.Blue, AutonomousPosition.Backstage)

@Autonomous(group = "Preloads")
class BlueFarPreloads : Preloads(AutonomousSide.Blue, AutonomousPosition.Stacks)