package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.NullAction
import com.acmerobotics.roadrunner.SequentialAction

data class AutoActions(
    val purple: Action,
    val yellow: Action,
    val cycle: Action = NullAction(),
    val park: Action = NullAction(),
    val white: Action = NullAction()
)