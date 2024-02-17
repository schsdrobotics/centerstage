package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit
import kotlin.math.abs

open class MoveDeposit(val state: Deposit.State, private val deposit: Deposit) : CommandBase() {
    val target = Deposit.Kinematics.inverse(state)

    init { addRequirements(deposit) }

    override fun initialize() = deposit.to(state)

    override fun isFinished() = listOf(
        target.left - deposit.angles.left,
        target.right - deposit.angles.right
    )
            .map { abs(it) }
            .map { it < THRESHOLD }
            .reduce { a, b -> a && b }

    companion object {
        const val THRESHOLD = 3.0
    }
}