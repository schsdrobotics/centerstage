package org.firstinspires.ftc.teamcode.autonomous

import com.acmerobotics.roadrunner.Pose2d

enum class AutonomousPosition(val start: Pose2d) { Close(Close.start), Far(Far.start) }