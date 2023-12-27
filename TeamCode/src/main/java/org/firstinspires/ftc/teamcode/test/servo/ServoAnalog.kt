package org.firstinspires.ftc.teamcode.test.servo

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import kotlin.math.max

@TeleOp
class ServoAnalog : OpMode() {
    val servo by lazy { hardwareMap["servo"] as Servo }

    override fun init() { servo }

    override fun loop() {
        val magnitude = gamepad1.right_trigger.toDouble()
        val value = max(0.0, 35.0 * DEGREE * magnitude)

        servo.position = value

        telemetry.addData("value", value)
    }

    companion object {
        private const val DEGREE = 1.0 / 300.0
    }
}