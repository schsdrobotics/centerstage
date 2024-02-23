package org.firstinspires.ftc.teamcode.autonomous.opmodes.preloads

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance.Blue
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance.Red
import org.firstinspires.ftc.teamcode.autonomous.framework.Side.Backstage
import org.firstinspires.ftc.teamcode.autonomous.framework.Side.Stacks
import org.firstinspires.ftc.teamcode.autonomous.opmodes.preloads.close.ClosePreloadsBase

@Autonomous(group = "Preloads")
class RedClosePreloads : ClosePreloadsBase(Red, Backstage)

@Autonomous(group = "Preloads")
class RedFarPreloads : ClosePreloadsBase(Red, Stacks)

@Autonomous(group = "Preloads")
class BlueClosePreloads : ClosePreloadsBase(Blue, Backstage)

@Autonomous(group = "Preloads")
class BlueFarPreloads : ClosePreloadsBase(Blue, Stacks)