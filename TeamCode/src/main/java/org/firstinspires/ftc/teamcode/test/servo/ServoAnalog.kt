package org.firstinspires.ftc.teamcode.test.servo

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.ServoImplEx
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import kotlin.math.max
import kotlin.math.min

@TeleOp
class ServoAnalog : OpMode() {
    val servo by lazy { hardwareMap["servo"] as Servo }

    override fun init() { servo }

    override fun loop() {
        val magnitude = max(gamepad1.right_trigger.toDouble(), 0.5)
        val value = max(0.0, 45.0 * DEGREE * magnitude)

        servo.position = value

        telemetry.addData("value", value)
        telemetry.addData("trigger", magnitude)
        telemetry.addData("current", hardwareMap.getAll(LynxModule::class.java).fold(0.0) { acc, it -> acc + it.getCurrent(CurrentUnit.AMPS) })
    }

    companion object {
        private const val DEGREE = 1.0 / 300.0
    }
}