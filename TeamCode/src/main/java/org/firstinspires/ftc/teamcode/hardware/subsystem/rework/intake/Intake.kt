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

    var target = (UP + DOWN) / 2.0
    var count = 0

    init {
        motor.direction = DcMotorSimple.Direction.REVERSE
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        chads.forEach { it.direction = Servo.Direction.REVERSE }
    }

    fun forward() { motor.power = 1.0 }
    fun reverse() { motor.power = -1.0 }
    fun stop() { motor.power = 0.0 }

    fun to(position: Double) { target = position }

    fun up() = to(UP)
    fun down() = to(DOWN)

    fun next() { this.target = (DOWN + SLICE * (count % STEPS - 1)).also { count++ } }

    override fun periodic() { chads.forEach { it.position = target } }

    companion object {
        const val STEPS = 5.0
        const val DOWN = 0.09
        const val UP = 0.23

        const val SLICE = (UP - DOWN) / STEPS
    }
}