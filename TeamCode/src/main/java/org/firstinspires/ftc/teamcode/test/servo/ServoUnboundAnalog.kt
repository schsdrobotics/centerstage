package org.firstinspires.ftc.teamcode.test.servo

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import kotlin.math.max

@TeleOp
class ServoUnboundAnalog : OpMode() {
    val spatula by lazy { hardwareMap["puncher"] as Servo }

    override fun init() { listOf(spatula).forEach { it.direction = Servo.Direction.REVERSE } }

    override fun loop() {
        val magnitude = gamepad1.right_trigger.toDouble()

        listOf(spatula).forEach { it.position = magnitude }

        telemetry.addData("trigger", magnitude)
        telemetry.addData("current", hardwareMap.getAll(LynxModule::class.java).fold(0.0) { acc, it -> acc + it.getCurrent(CurrentUnit.AMPS) })
    }
}