package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake

import org.firstinspires.ftc.teamcode.hardware.Robot.IntakeHardware.Configuration.RADIUS
import org.firstinspires.ftc.teamcode.hardware.Robot.IntakeHardware.Configuration.UP_ANGLE
import org.firstinspires.ftc.teamcode.hardware.Robot.IntakeHardware.arm
import org.firstinspires.ftc.teamcode.hardware.Robot.IntakeHardware.motor
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.EfficientSubsystem
import kotlin.math.abs
import kotlin.math.asin

class Intake : EfficientSubsystem() {
    var target = UP_ANGLE
    var actual = 0.0
    var ideal = 0.0

    override fun read() { }

    override fun write() {
        if (arm.angle != target) arm.turnToAngle(target)

        if (abs(ideal - actual) > 0.3) {
            motor.power = ideal
            actual = ideal
        }
    }

    override fun reset() { motor.power = 0.0; arm.turnToAngle(UP_ANGLE) }

    override fun periodic() { }

    fun forward(speed: Double) { ideal = speed }

    fun reverse(speed: Double) { ideal = -speed }

    fun stop() { ideal = 0.0 }


    fun angleForHeight(height: Double) = Math.toDegrees(asin(height / RADIUS))

    fun target(angle: Double) { target = angle }

    companion object {
        // 33, 28, 20, 16, 10
        const val FIVE = 33
        const val FOUR = 28
        const val THREE = 20
        const val TWO = 16
        const val ONE = 10
    }
}