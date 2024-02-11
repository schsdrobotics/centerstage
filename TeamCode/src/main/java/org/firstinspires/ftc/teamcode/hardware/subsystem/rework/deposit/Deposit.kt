package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit

import com.arcrobotics.ftclib.command.SubsystemBase
import com.arcrobotics.ftclib.hardware.SimpleServo
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.util.extensions.deg

class Deposit(val hw: HardwareMap, val telemetry: Telemetry) : SubsystemBase() {
    val right by lazy { SimpleServo(hw, "depositRight", 0.0, SERVO_RANGE).also { it.inverted = true } }
    val left by lazy  { SimpleServo(hw, "depositLeft", 0.0, SERVO_RANGE) }

    var state = State(0.deg, 0.deg)

    fun flip(omega: Double) = to(omega, state.horizontal)
    fun turn(theta: Double) = to(state.vertical, theta)

    fun to(state: State) = to(state.vertical, state.horizontal)

    // TODO: add bounds checks
    fun to(vertical: Double, horizontal: Double) {
        state = State(vertical, horizontal)

        val composed = vertical(vertical) + horizontal(horizontal)

        right.turnToAngle(composed.right)
        left.turnToAngle(composed.left)
    }

    fun vertical(angle: Double) = Range
            .clip(angle, TRANSFER_BOUND, SCORE_BOUND)
            .let { Offset(it, it) }

    fun horizontal(angle: Double) = Range
            .clip(angle, -HORIZONTAL_BOUND, HORIZONTAL_BOUND)
            .let { Offset(it, -it) }

    data class State(val vertical: Double, val horizontal: Double)
    data class Offset(val left: Double, val right: Double) {
        operator fun plus(x: Offset) = Offset(left + x.left, right + x.right)
    }

    companion object {
        const val SERVO_RANGE = 355.0 // degrees

        const val HORIZONTAL_BOUND = 135.0 // +/- degrees

        const val TRANSFER_BOUND = 0.0 // degrees
        const val SCORE_BOUND = 100.0 // degrees
    }
}