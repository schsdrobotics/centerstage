package org.firstinspires.ftc.teamcode.test

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.ContinuousAxonServo
import kotlin.math.max

@TeleOp
class ServoCurrent : OpMode() {
    private val servo by lazy { ContinuousAxonServo(hardwareMap, "servo") }

    override fun init() { servo }
    override fun loop() {
        val halved = (gamepad1.right_trigger.toDouble()) / 2.0
        val full = (gamepad1.left_trigger.toDouble())

        servo.power = max(halved, full)

        telemetry.addData("power", servo.position * 100.0)
        telemetry.addData("current (mA)", servo.servo)
    }
}
