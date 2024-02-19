package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit

import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.ALIGN_ANGLE
import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.HORIZONTAL_BOUND
import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.HORIZONTAL_OFFSET
import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.SCORE_ANGLE
import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.TRANSFER_ANGLE
import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.VERTICAL_OFFSET
import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.angles
import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.left
import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.right
import org.firstinspires.ftc.teamcode.hardware.Robot.DriveHardware.angle
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.EfficientSubsystem
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import kotlin.math.abs
import kotlin.math.round


class Deposit(val telemetry: Telemetry, val lift: Lift) : EfficientSubsystem() {
    private fun heading() = angle + 180

    private var happy = false

    var state = State(0.0, TRANSFER_ANGLE)
        set(value) { field = State(value.vertical + VERTICAL_OFFSET, value.horizontal + HORIZONTAL_OFFSET) }

    var targets = Kinematics.inverse(state)


    fun flip(omega: Double) = to(omega, state.horizontal)
    fun turn(theta: Double) = to(state.vertical, theta)

    fun to(state: State) = to(state.vertical, state.horizontal)

    // TODO: add bounds checks
    fun to(vertical: Double, horizontal: Double) { state = State(vertical, horizontal) }

    fun default() {

    }

    fun sad() { happy = false }

    override fun periodic() {
        val locked = Kinematics.lock(heading(), 0.0)

        if (!lift.cleared) happy = true

        targets = if (lift.cleared && happy) {
            Kinematics.inverse(State(state.vertical, if (abs(locked) > HORIZONTAL_BOUND + 10) state.horizontal else -locked))
        } else {
            Kinematics.inverse(state)
        }

        telemetry.addData("left angle", angles.left)
        telemetry.addData("right angle", angles.right)
        telemetry.addData("target state", state)
        telemetry.addData("locked angle", locked)
    }

    override fun read() {
        angles.read()
    }

    override fun write() {
        right.turnToAngle(targets.right)
        left.turnToAngle(targets.left)
    }

    override fun reset() = Unit

    data class State(val vertical: Double, val horizontal: Double)
    data class Offset(val left: Double, val right: Double) {
        operator fun plus(x: Offset) = Offset(left + x.left, right + x.right)
    }

    class Angles(hw: HardwareMap) {
        val rightReader by lazy { hw["deposit.right.pot"] as AnalogInput }
        val leftReader by lazy { hw["deposit.left.pot"] as AnalogInput }

        var right = 0.0
        var left = 0.0

        fun read() {
            // invert + offset
            right = 360.0 - ((360.0 - round(rightReader.voltage / 3.3 * 360)) - 20.0) + 45.0 - 90.0 - 11.0 + 22.0
            // offset
            left = 360.0 - (round(leftReader.voltage / 3.3 * 360) - 15.0) + 45.0 - 90.0 - 9.0 + 16.0
        }
    }

    object Kinematics {
        fun vertical(angle: Double) = Range.clip(angle, ALIGN_ANGLE, SCORE_ANGLE).let { Offset(it, it) }

        fun horizontal(angle: Double) = Range.clip(angle, -HORIZONTAL_BOUND, HORIZONTAL_BOUND).let { Offset(it, -it) }

        fun inverse(state: State) = vertical(state.vertical) + horizontal(state.horizontal)

        fun lock(heading: Double, target: Double) = findShortestDistance(heading, target)

        fun findShortestDistance(a: Double, b: Double): Double {
            val difference: Double = a - b

            if (difference > 180) {
                return -360 + difference
            } else if (difference < -180) {
                return 360 + difference
            }

            return difference
        }
    }
}
