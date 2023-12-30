package org.firstinspires.ftc.teamcode.test.servo

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import kotlin.math.max

@TeleOp
class ServoUnboundAnalog : OpMode() {
    val servo by lazy { hardwareMap["servo"] as Servo }

    override fun init() {
        servo.direction = Servo.Direction.REVERSE
        servo.scaleRange(0.3, 0.6)
    }

    override fun loop() {
        val magnitude = gamepad1.right_trigger.toDouble()

        servo.position = magnitude

        telemetry.addData("trigger", magnitude)
        telemetry.addData("current", hardwareMap.getAll(LynxModule::class.java).fold(0.0) { acc, it -> acc + it.getCurrent(CurrentUnit.AMPS) })
    }
}