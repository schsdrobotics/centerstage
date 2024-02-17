package org.firstinspires.ftc.teamcode.test.servo

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class ServoUnboundAnalog : OpMode() {
    val leftChad by lazy { hardwareMap["puncher"] as Servo }

    val servos by lazy { listOf(leftChad) }

    override fun init() {  }

    override fun loop() {
        val magnitude = gamepad1.right_trigger.toDouble()

        servos.forEach { it.position = magnitude }

        telemetry.addData("magnitude", magnitude)
    }
}