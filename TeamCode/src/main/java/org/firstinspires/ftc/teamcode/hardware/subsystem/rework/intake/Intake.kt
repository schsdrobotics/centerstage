package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class Intake(val hw: HardwareMap) : SubsystemBase() {
    private val rightChad by lazy { hw["rightChad"] as Servo }
    private val leftChad by lazy { hw["leftChad"] as Servo }
    private val chads by lazy { listOf(leftChad, rightChad) }

    private val motor by lazy { hw["perp"] as DcMotor }

    var target = UP
    var count = 0

    init {
        motor.direction = DcMotorSimple.Direction.REVERSE
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    fun forward(speed: Double) { motor.power = speed }
    fun reverse(speed: Double) { motor.power = -speed }
    fun stop() { motor.power = 0.0 }

    fun to(position: Double) { target = position }

    fun up() = to(UP)
    fun down() = to(DOWN)

    fun next() {
        if (count % STEPS == 0.0) {
            this.target = UP
        } else {
            this.target = (DOWN + SLICE * (count % STEPS + 1))
        }

        count++
    }

    override fun periodic() { chads.forEach { it.position = target } }

    companion object {
        const val STEPS = 7.0
        const val UP = 0.97
        const val DOWN = UP - 0.16

        const val SLICE = (0.87 - DOWN) / STEPS
    }
}