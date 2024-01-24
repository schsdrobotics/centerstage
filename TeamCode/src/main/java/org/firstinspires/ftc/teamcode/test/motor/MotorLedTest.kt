package org.firstinspires.ftc.teamcode.test.motor

import com.acmerobotics.roadrunner.now
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import kotlin.math.abs
import kotlin.math.sin

@TeleOp
class MotorLedTest : OpMode() {
    val motor by lazy { hardwareMap["led"] as DcMotor }

    override fun init() {}

    override fun loop() {
        motor.power = abs(sin(now()))
    }
}