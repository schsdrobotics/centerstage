package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.HORIZONTAL_OFFSET
import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.VERTICAL_OFFSET
import org.firstinspires.ftc.teamcode.hardware.Robot.telemetry
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit
import kotlin.math.abs


open class MoveDeposit(val state: Deposit.State, private val deposit: Deposit, val shouldCheck: Boolean = true) : CommandBase() {
    val target = Deposit.Kinematics.inverse(Deposit.State(state.horizontal + HORIZONTAL_OFFSET, state.vertical + VERTICAL_OFFSET))

    init { addRequirements(deposit) }

    override fun initialize() = deposit.to(state)

    override fun isFinished() = if (shouldCheck) {
        listOf(
            target.left - Robot.DepositHardware.angles.left,
            target.right - Robot.DepositHardware.angles.right
        )
            .also { telemetry.addData("differences", it) }
            .map { abs(it) }
            .also { telemetry.addData("abs", it) }
            .map { it < THRESHOLD }
            .also { telemetry.addData("bools", it) }
            .reduce { a, b -> a && b }
            .also { telemetry.addData("single", it) }
    } else {
        true
    }

    companion object {
        const val THRESHOLD = 7.0
    }
}