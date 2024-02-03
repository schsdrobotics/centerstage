package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.NullAction

data class AutoActions(val purple: Action, val yellow: Action, val park: Action = NullAction(), val cycle: Action = NullAction())