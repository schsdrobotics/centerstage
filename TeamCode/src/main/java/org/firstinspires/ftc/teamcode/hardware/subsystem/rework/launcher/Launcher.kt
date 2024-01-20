package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class Launcher(val hw: HardwareMap) : SubsystemBase() {
    private val servo by lazy {
        val x = hw["launcher"] as Servo

        x.direction = Servo.Direction.REVERSE
        x.position = target
        x
    }
    var target = HOLD

    fun launch() { target = RELEASE }
    fun hold() { target = HOLD }

    override fun periodic() { servo.position = target }

    companion object {
        const val HOLD = 0.35
        const val RELEASE = 1.0
    }
}