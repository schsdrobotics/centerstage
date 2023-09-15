package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE

@TeleOp
class SkystoneLift : OpMode() {
    private val right by lazy { hardwareMap.dcMotor["right"] }
    private val left by lazy { hardwareMap.dcMotor["left"] }

    override fun init() {
        right.direction = REVERSE

        right.zeroPowerBehavior = BRAKE
        left.zeroPowerBehavior = BRAKE
    }

    override fun loop() {
        val power = gamepad1.left_stick_y

        left.power = power.toDouble()
        right.power = power.toDouble()
    }
}