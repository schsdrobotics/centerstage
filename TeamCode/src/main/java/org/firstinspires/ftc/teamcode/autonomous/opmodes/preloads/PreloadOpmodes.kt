package org.firstinspires.ftc.teamcode.autonomous.opmodes.preloads

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.autonomous.framework.Side.Backstage
import org.firstinspires.ftc.teamcode.autonomous.framework.Side.Stacks
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance.Blue
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance.Red

@Autonomous(group = "Preloads")
class RedClosePreloads : PreloadsBase(Red, Backstage)

@Autonomous(group = "Preloads")
class RedFarPreloads : PreloadsBase(Red, Stacks)

@Autonomous(group = "Preloads")
class BlueClosePreloads : PreloadsBase(Blue, Backstage)

@Autonomous(group = "Preloads")
class BlueFarPreloads : PreloadsBase(Blue, Stacks)