package org.firstinspires.ftc.teamcode.autonomous.purplePixel

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide

@Autonomous(group = "Preloads")
class BlueClosePreloads : Preloads(AutonomousSide.Blue, close = true)
