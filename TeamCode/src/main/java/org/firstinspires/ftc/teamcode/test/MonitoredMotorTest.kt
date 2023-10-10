package org.firstinspires.ftc.teamcode.test

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.DecayFunction
import org.firstinspires.ftc.teamcode.hardware.MonitoredMotor

@TeleOp
class MonitoredMotorTest : OpMode() {
    private val motor by lazy { MonitoredMotor(hardwareMap, "motor", 250.0, decay)  }

    override fun init() { }

    override fun loop() {
        motor.power = 0.125

        motor.tick()

        telemetry.addData("power", motor.power)
        telemetry.addData("current (mA)", motor.current)
    }

    companion object {
        val decay: DecayFunction = { power, current, limit -> if (current > limit) power / 2 else power }
    }
}