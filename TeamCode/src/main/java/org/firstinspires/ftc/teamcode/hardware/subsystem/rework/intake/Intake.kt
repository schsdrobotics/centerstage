package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake

import com.arcrobotics.ftclib.command.SubsystemBase
import com.arcrobotics.ftclib.hardware.SimpleServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import kotlin.math.asin

class Intake
(val hw: HardwareMap) : SubsystemBase() {
    private val motor by lazy { hw["perp"] as DcMotor }
    val arm by lazy { SimpleServo(hw, "arm", DOWN_ANGLE, RANGE) }

    var target = UP_ANGLE

    init { motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE }

    fun forward(speed: Double) { motor.power = speed }
    fun reverse(speed: Double) { motor.power = -speed }
    fun stop() { motor.power = 0.0 }

    fun target(angle: Double) { target = angle }

    override fun periodic() { angle(target) }

    // height: inches
    fun angleForHeight(height: Double) = Math.toDegrees(asin(height / RADIUS))

    fun angle(angle: Double) { arm.turnToAngle(angle) }

    companion object {
        const val UP_ANGLE = 75.0 // degrees
        const val DOWN_ANGLE = 0.0 // degrees

        const val RANGE = 300.0 // degrees

        const val RADIUS = 6.0 // in
    }
}