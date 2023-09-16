package org.firstinspires.ftc.teamcode.test

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.AxonServo

@TeleOp
class AxonServoTesting : OpMode() {
    private val servos by lazy { List(4) { AxonServo(hardwareMap, "servo$it") } }

    override fun init() { }

    override fun loop() {
        val input = gamepad1.left_stick_x.toDouble()

        servos.forEach {
            it.position = input
            telemetry.addData("position", it.position)
        }

        telemetry.addData("x", input)
    }
}