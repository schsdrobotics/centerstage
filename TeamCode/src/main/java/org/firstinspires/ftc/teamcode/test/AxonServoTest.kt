package org.firstinspires.ftc.teamcode.test

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.AxonServo

@TeleOp
class AxonServoTest : OpMode() {
    private val servo by lazy { AxonServo(hardwareMap, "servo") }

    override fun init() { }

    override fun loop() {
        val input = gamepad1.left_stick_x.toDouble()

        servo.position = input

        telemetry.addData("position", servo.position)
        telemetry.addData("input", input)
    }
}