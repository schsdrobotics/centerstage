package org.firstinspires.ftc.teamcode.test

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo
import org.firstinspires.ftc.teamcode.hardware.ContinuousAxonServo
import kotlin.math.max

@TeleOp
class TwoServos : OpMode() {

    private val servo by lazy { ContinuousAxonServo(hardwareMap, "servo") }

    private val contServo by lazy { hardwareMap["contServo"] as CRServo }

    override fun init() {
        servo
        contServo
    }

    override fun loop() {
        val halved = (gamepad1.right_trigger.toDouble()) / 2.0
        val full = (gamepad1.left_trigger.toDouble())

        servo.position = max(halved, full)
        contServo.power = max(halved, full)
    }
}