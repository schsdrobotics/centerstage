package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.NullAction
import com.acmerobotics.roadrunner.SequentialAction

val NullAutoActions = AutoActions(NullAction(), NullAction(), NullAction(), NullAction(), NullAction())

data class AutoActions(
    val purple: Action,
    val yellow: Action,
    val backdrop: Action,
    val stacks: Action,
    val park: Action,
)