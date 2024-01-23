package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.transfer

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.ColorRangeSensor
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

class Transfer(val hw: HardwareMap, val telemetry: Telemetry) : SubsystemBase() {
    val color by lazy { hw["color"] as ColorRangeSensor }

    fun count(): Nothing = TODO()

    override fun periodic() {

    }

    enum class PixelCount(val threshold: Double) {
        NONE(1.0),
        ONE(0.5),
        TWO(0.0)
    }
}