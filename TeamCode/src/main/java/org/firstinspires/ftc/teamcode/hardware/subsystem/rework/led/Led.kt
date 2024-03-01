package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.led

import org.firstinspires.ftc.teamcode.hardware.Robot.LedHardware.led
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.EfficientSubsystem

class Led : EfficientSubsystem() {
    var count = 0

    init { led.power = 0.9 }

    override fun periodic() { }
    override fun read() { }

    override fun write() {
        if (count % 100 == 0) led.power = 0.9
    }

    override fun reset() { led.power = 0.0 }
}