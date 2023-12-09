package org.firstinspires.ftc.teamcode.test

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.hardware.axon.AxonServo
import kotlin.math.pow

@TeleOp
class AxonServoTesting : OpMode() {
    private val servo by lazy { AxonServo(hardwareMap, "servo") }
    private val timer = ElapsedTime()
    private val LINE = 5000.0
    override fun init() {}

    override fun init_loop() {
        fun f(x: Double) = (2.0).pow(1.1 * x) - (1.0 / 6.0) * x - 1.035
        val x = 1 - (servo.position / 360.0)
        val value = f(x)

        servo.position = value

        telemetry.addData("x", x)
        telemetry.addData("value", value)
    }

    override fun start() = timer.reset()

    fun reset() {
        servo.position = 0.0
    }
    fun execute() {
        fun f(x: Double) = -(2.0).pow(x) + (1.0 / 6.0) * x + 2.025

        val value = f(servo.position / 360.0)

        telemetry.addData("x", servo.position / 360.0)
        telemetry.addData("value", value)

        servo.position = if (value.isNaN()) 0.0 else value
    }

    override fun loop() {
        execute()
    }
}