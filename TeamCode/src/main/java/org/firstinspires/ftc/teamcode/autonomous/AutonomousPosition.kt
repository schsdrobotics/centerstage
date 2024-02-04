package org.firstinspires.ftc.teamcode.autonomous

import com.acmerobotics.roadrunner.Pose2d
import org.firstinspires.ftc.teamcode.autonomous.framework.*

enum class AutonomousPosition(val start: Pose2d) { Backstage(Close.start), Stacks(Far.start) }