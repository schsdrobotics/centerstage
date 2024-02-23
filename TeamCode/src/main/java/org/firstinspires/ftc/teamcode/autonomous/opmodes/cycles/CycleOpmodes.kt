package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.autonomous.framework.Side
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.close.CloseCyclesBase
import org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.close.CloseCyclesPathBase
import org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.far.FarCyclesBase

@Autonomous(group = "Cycles")
class RedCloseCyclesPath : CloseCyclesPathBase(Alliance.Red, Side.Backstage)

@Autonomous(group = "Cycles")
class RedCloseCycles : CloseCyclesBase(Alliance.Red, Side.Backstage)

@Autonomous(group = "Cycles")
class RedFarCycles : FarCyclesBase(Alliance.Red, Side.Stacks)

@Autonomous(group = "Cycles")
class BlueCloseCycles : CloseCyclesBase(Alliance.Blue, Side.Backstage)

@Autonomous(group = "Cycles")
class BlueFarCycles : FarCyclesBase(Alliance.Blue, Side.Stacks)