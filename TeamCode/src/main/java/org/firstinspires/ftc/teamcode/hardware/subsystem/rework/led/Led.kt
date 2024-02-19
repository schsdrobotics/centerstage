package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.led

import org.firstinspires.ftc.teamcode.hardware.Robot.LedHardware.led
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.EfficientSubsystem

class Led : EfficientSubsystem() {
    init { led.power = 0.9 }

    override fun periodic() { }
    override fun read() { }

    override fun write() { }

    override fun reset() { led.power = 0.0 }
}