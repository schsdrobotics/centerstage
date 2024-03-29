package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.Side
import org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.bases.FarCyclesBase

//@Autonomous(group = "Cycles")
//class RedCloseCycles : CloseCyclesBase(Alliance.Red, Side.Backstage)

@Autonomous(group = "Cycles")
class RedFarCycles : FarCyclesBase(Alliance.Red, Side.Frontstage)

//@Autonomous(group = "Cycles")
//class BlueCloseCycles : CloseCyclesBase(Alliance.Blue, Side.Backstage)

@Autonomous(group = "Cycles")
class BlueFarCycles : FarCyclesBase(Alliance.Blue, Side.Frontstage)