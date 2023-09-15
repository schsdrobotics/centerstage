package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.pow

private typealias Scaling = (Float, Float, Float) -> Double

class Drivetrain(private val opMode: OpMode, private val gamepad: Gamepad = opMode.gamepad1) : Subsystem {
    private val multiplierMap: Map<DcMotor, MotorMultipliers>
    private val timer = ElapsedTime()


    init {
        val hardwareMap = opMode.hardwareMap

        // z doesn't need a multiplier since everything is +1
        multiplierMap = hashMapOf(
            hardwareMap.dcMotor["frontLeft"] to MotorMultipliers(+1, +1),
            hardwareMap.dcMotor["frontRight"] to MotorMultipliers(+1, -1),
            hardwareMap.dcMotor["backLeft"] to MotorMultipliers(-1, +1),
            hardwareMap.dcMotor["backRight"] to MotorMultipliers(-1, -1)
        )

        multiplierMap.keys.forEach {
            it.mode = DcMotor.RunMode.RUN_USING_ENCODER
            it.direction = DcMotorSimple.Direction.FORWARD
        }
    }

    @Suppress("NAME_SHADOWING")
    fun move(x: Float = 0f, y: Float = 0f, z: Float = 0f,
             scaleFunction: Scaling = TELEOP_SCALING) {
        val total = abs(x) + abs(y) + abs(z)

        if (total == 0f) return stop() // prevent division by 0

        setMotorPowers { _, mults -> (mults.x * x + mults.y * y + z) * scaleFunction(x, y, z) * SCALE_FACTOR / total }
    }

    fun rotate(factor: Double) {
        move(0f, 0f, factor.toFloat())
    }

    private fun stop() = multiplierMap.keys.forEach { it.power = 0.0 }

    override fun loop() {
        move(
            x = gamepad.left_stick_x,
            y = -gamepad.left_stick_y, // for some atrocious reason, down is positive
            z = gamepad.right_stick_x
        )
    }

    data class MotorMultipliers(val x: Int, val y: Int)

    private inline fun setMotorPowers(power: (DcMotor, MotorMultipliers) -> Double) {
        multiplierMap.forEach { (motor, mults) ->
            motor.power = power(motor, mults)
        }
    }

    fun move(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0, scaling: Scaling = TELEOP_SCALING) {
        move(x.toFloat(), y.toFloat(), z.toFloat(), scaling)
    }

    companion object {
        const val SCALE_FACTOR = 1.0
        val TELEOP_SCALING: Scaling = { x, y, z -> max(0.2 , max(hypot(x, y), abs(z)).pow(2f).toDouble()) }
        val LINEAR_SCALING: Scaling = { x, y, z -> max(hypot(x, y), abs(z)).toDouble() }
    }
}
