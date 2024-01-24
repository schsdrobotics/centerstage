package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.led

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.acmerobotics.roadrunner.now
import kotlin.math.abs
import kotlin.math.sin

class Led(val hw: HardwareMap) : SubsystemBase() {
    val led by lazy { hw["led"] as DcMotor }

    init { led.power = abs(sin(now())) }

    override fun periodic() { led.power = abs(sin(now())) }
}