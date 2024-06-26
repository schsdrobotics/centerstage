package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
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


class Deposit(val telemetry: Telemetry, val lift: Lift, val gamepad: GamepadEx? = null) : EfficientSubsystem() {
    private fun heading() = angle + 180

    private var happy = false

    val align
        get() = State(VERTICAL_OFFSET + TRANSFER_ANGLE, HORIZONTAL_OFFSET)

    var headingTarget = 270.0

    var adjustment = State(0.0, 0.0)

    var state = align
        set(value) { field = State(value.vertical + VERTICAL_OFFSET, value.horizontal + HORIZONTAL_OFFSET) }

    var targets = Kinematics.inverse(align)

    fun flip(omega: Double) = to(omega, state.horizontal)
    fun turn(theta: Double) = to(state.vertical, theta)

    fun to(state: State) = to(state.vertical, state.horizontal)

    // TODO: add bounds checks
    fun to(vertical: Double, horizontal: Double) { state = State(vertical, horizontal) }

    fun sad() { happy = false }

    fun verticallyAdjust(angle: Double) { VERTICAL_OFFSET = Range.clip(VERTICAL_OFFSET + angle, -2.0, 7.0) }

    override fun periodic() {
        val locked = Kinematics.lock(heading(), headingTarget)

        if (gamepad != null) {
            if ((gamepad.rightX != 0.0 || gamepad.rightY != 0.0) && gamepad.isDown(GamepadKeys.Button.RIGHT_STICK_BUTTON)) {
                adjustment = State(-gamepad.rightY * 10.0, gamepad.rightX * 10.0)
            }
        }

        if (!lift.cleared) happy = true

        targets = if (state.vertical == SCORE_ANGLE + VERTICAL_OFFSET) {
            Kinematics.inverse(State(state.vertical, if (abs(locked) > HORIZONTAL_BOUND + 20) state.horizontal else -locked))
        } else {
            Kinematics.inverse(state + adjustment)
        }

        telemetry.addData("adjustment", adjustment)
        telemetry.addData("left angle", angles.left)
        telemetry.addData("right angle", angles.right)
        telemetry.addData("target angles", Kinematics.inverse(state))
        telemetry.addData("target state", state)
        telemetry.addData("locked angle", locked)
        telemetry.addData("happy", happy)
    }

    override fun read() {
        angles.read()
    }

    override fun write() {
        right.turnToAngle(targets.right)
        left.turnToAngle(targets.left)

//        if (state.vertical > 50 && !lift.cleared) {
//            val output = Kinematics.inverse(align + adjustment)
//            telemetry.addData("output", output)
//            right.turnToAngle(output.right)
//            left.turnToAngle(output.left)
//        } else {
//            right.turnToAngle(targets.right)
//            left.turnToAngle(targets.left)
//        }
    }

    override fun reset() = Unit

    data class State(val vertical: Double, val horizontal: Double) {
        operator fun plus(x: State) = State(x.vertical + vertical, x.horizontal + horizontal)
    }
    data class Offset(val left: Double, val right: Double) {
        operator fun plus(x: Offset) = Offset(left + x.left, right + x.right)
    }

    class Angles(hw: HardwareMap) {
        val rightReader by lazy { hw["deposit.right.pot"] as AnalogInput }
        val leftReader by lazy { hw["deposit.left.pot"] as AnalogInput }

        var right = 0.0
        var left = 0.0

        fun read() {
            // errors should be +/- 2
            right = (360.0 - round(rightReader.voltage / 3.3 * 360)) - 1.0
            left = (round(leftReader.voltage / 3.3 * 360)) - 4.0
        }
    }

    object Kinematics {
        fun vertical(angle: Double) = Range.clip(angle, ALIGN_ANGLE, SCORE_ANGLE).let { Offset(it, it) }

        fun horizontal(angle: Double) = Range.clip(angle, -HORIZONTAL_BOUND, HORIZONTAL_BOUND).let { Offset(-it, it) }

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
