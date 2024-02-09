package org.firstinspires.ftc.teamcode.autonomous.preloads

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousPosition.Backstage
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousPosition.Stacks
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide.Blue
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide.Red

@Autonomous(group = "Preloads")
class RedClosePreloads : Preloads(Red, Backstage)

@Autonomous(group = "Preloads")
class RedFarPreloads : Preloads(Red, Stacks)

@Autonomous(group = "Preloads")
class BlueClosePreloads : Preloads(Blue, Backstage)

@Autonomous(group = "Preloads")
class BlueFarPreloads : Preloads(Blue, Stacks)