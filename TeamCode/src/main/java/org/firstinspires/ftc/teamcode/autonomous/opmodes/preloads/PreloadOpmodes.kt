package org.firstinspires.ftc.teamcode.autonomous.opmodes.preloads

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance.Blue
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance.Red
import org.firstinspires.ftc.teamcode.autonomous.framework.Side.Backstage
import org.firstinspires.ftc.teamcode.autonomous.framework.Side.Frontstage
import org.firstinspires.ftc.teamcode.autonomous.opmodes.preloads.bases.ClosePreloadsBase
import org.firstinspires.ftc.teamcode.autonomous.opmodes.preloads.bases.FarPreloadsBase

@Autonomous(group = "Preloads")
class RedClosePreloads : ClosePreloadsBase(Red, Backstage)

@Autonomous(group = "Preloads")
class RedFarPreloads : FarPreloadsBase(Red, Frontstage)

@Autonomous(group = "Preloads")
class BlueClosePreloads : ClosePreloadsBase(Blue, Backstage)

@Autonomous(group = "Preloads")
class BlueFarPreloads : FarPreloadsBase(Blue, Frontstage)