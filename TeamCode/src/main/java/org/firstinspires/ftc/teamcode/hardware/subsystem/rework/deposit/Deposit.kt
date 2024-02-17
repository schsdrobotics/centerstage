package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit

import com.arcrobotics.ftclib.command.SubsystemBase
import com.arcrobotics.ftclib.hardware.SimpleServo
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.util.extensions.deg
import kotlin.math.round

class Deposit(val hw: HardwareMap, val telemetry: Telemetry) : SubsystemBase() {
    val right by lazy { SimpleServo(hw, "deposit.right", 0.0, SERVO_RANGE).also { it.inverted = true } }
    val left by lazy  { SimpleServo(hw, "deposit.left", 0.0, SERVO_RANGE) }
    var state = State(0.deg, 0.deg)
    val angles = Angles(hw)

    fun flip(omega: Double) = to(omega, state.horizontal)
    fun turn(theta: Double) = to(state.vertical, theta)

    fun to(state: State) = to(state.vertical, state.horizontal)

    // TODO: add bounds checks
    fun to(vertical: Double, horizontal: Double) { state = State(vertical, horizontal) }

    override fun periodic() {
        val targets = Kinematics.inverse(state)

        right.turnToAngle(targets.right)
        left.turnToAngle(targets.left)

        telemetry.addData("left angle", angles.left)
        telemetry.addData("right angle", angles.right)
    }

    data class State(val vertical: Double, val horizontal: Double)
    data class Offset(val left: Double, val right: Double) {
        operator fun plus(x: Offset) = Offset(left + x.left, right + x.right)
    }

    inner class Angles(hw: HardwareMap) {
        val rightReader by lazy { hw["deposit.right.pot"] as AnalogInput }
        val leftReader by lazy { hw["deposit.left.pot"] as AnalogInput }

        // invert + offset
        val right: Double
            get() = (360.0 - round(rightReader.voltage / 3.3 * 360)) - 15.0


        // offset
        val left: Double
            get() = round(leftReader.voltage / 3.3 * 360) - 18.0
    }

    object Kinematics {
        fun vertical(angle: Double) = Range.clip(angle, ALIGN_ANGLE, SCORE_ANGLE).let { Offset(it, it) }

        fun horizontal(angle: Double) = Range.clip(angle, -HORIZONTAL_BOUND, HORIZONTAL_BOUND).let { Offset(it, -it) }

        fun inverse(state: State) = vertical(state.vertical) + horizontal(state.horizontal)
    }

    companion object {
        const val SERVO_RANGE = 355.0 // degrees

        const val HORIZONTAL_BOUND = 65.0 // +/- degrees

        const val ALIGN_ANGLE = 0.0 // degrees
        const val TRANSFER_ANGLE = 25.0 // degrees
        const val SCORE_ANGLE = 160.0 // degrees
    }
}