package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.NullAction
import com.acmerobotics.roadrunner.SequentialAction

val NoAuto = AutoActions(NullAction(), NullAction(), NoCycles(), NullAction())

data class Cycle(val backstage: Action, val stacks: Action)

val Cycle.composite
    get() = SequentialAction(backstage, stacks)


open class Cycles(val initial: Cycle, val rest: Cycle)
class NoCycles : Cycles(Cycle(NullAction(), NullAction()), Cycle(NullAction(), NullAction()))
class SingleCycle(stacks: Action, backstage: Action) : Cycles(Cycle(stacks = stacks, backstage = backstage), Cycle(NullAction(), NullAction()))

class YellowPixel(val backdrop: Action, val intermediate: Action)

open class AutoActions(
    val purple: Action,
    val yellow: Action,
    val cycles: Cycles,
    val park: Action,
    val extras: List<Action> = emptyList()
)

class PurpleAuto(purple: Action) : AutoActions(purple, NullAction(), NoCycles(), NullAction())
class PreloadAuto(purple: Action, yellow: Action) : AutoActions(purple, yellow, NoCycles(), NullAction())
class PreloadPark(purple: Action, yellow: Action, park: Action) : AutoActions(purple, yellow, NoCycles(), park)
