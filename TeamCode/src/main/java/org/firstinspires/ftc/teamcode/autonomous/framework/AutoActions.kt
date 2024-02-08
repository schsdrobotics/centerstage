package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.NullAction

val NoAuto = AutoActions(NullAction(), NullAction(), NullAction(), NullAction(), NullAction())

open class AutoActions(
    val purple: Action,
    val yellow: Action,
    val backdrop: Action,
    val stacks: Action,
    val park: Action,
)

sealed class PurpleAuto(purple: Action) : AutoActions(purple, NullAction(), NullAction(), NullAction(), NullAction())
sealed class PreloadAuto(purple: Action, yellow: Action) : AutoActions(purple, yellow, NullAction(), NullAction(), NullAction())
sealed class PreloadPark(purple: Action, yellow: Action, park: Action) : AutoActions(purple, yellow, NullAction(), NullAction(), park)
