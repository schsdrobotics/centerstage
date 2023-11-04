package org.firstinspires.ftc.teamcode.test

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.ServoImplEx
import org.firstinspires.ftc.teamcode.hardware.AxonServo

@TeleOp
class AxonServoTest : OpMode() {
    private val servo by lazy { hardwareMap["servo"] as ServoImplEx }

    override fun init() { }

    override fun loop() {
        val input = gamepad1.left_stick_x.toDouble()

        servo.position = input

        telemetry.addData("position", servo.position)
        telemetry.addData("input", input)
    }
}