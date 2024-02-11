package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake

import com.arcrobotics.ftclib.command.SubsystemBase
import com.arcrobotics.ftclib.hardware.SimpleServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import kotlin.math.asin

class Intake(val hw: HardwareMap) : SubsystemBase() {
    private val arm by lazy { SimpleServo(hw, "arm", DOWN_ANGLE, UP_ANGLE) }
    private val motor by lazy { hw["perp"] as DcMotor }

    var target = UP
    var count = 0

    init { motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE }

    fun forward(speed: Double) { motor.power = speed }
    fun reverse(speed: Double) { motor.power = -speed }
    fun stop() { motor.power = 0.0 }

    fun to(position: Double) { target = position }

    fun up() = to(UP)
    fun down() = to(DOWN)

    fun next() {
        if (count % 2 == 0) up() else down()

        count++
    }

    override fun periodic() { arm.position = target }

    // height: inches
    fun angleForHeight(height: Double) = Math.toDegrees(asin(height / RADIUS))

    fun angle(angle: Double) { arm.turnToAngle(angle) }

    companion object {
        const val UP_ANGLE = 95.0 // degrees
        const val DOWN_ANGLE = 0.0 // degrees

        const val CORRECTIVE = (1.0 / 0.97)
        const val UP = 0.97 * CORRECTIVE
        const val DOWN = (UP - 0.197) * CORRECTIVE

        const val RADIUS = 6.0 // in

    }
}