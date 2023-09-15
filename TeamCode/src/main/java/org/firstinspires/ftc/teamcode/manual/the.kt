package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import kotlin.math.*

@TeleOp
class the: OpMode() {
    private lateinit var frontLeft: DcMotor
    private lateinit var backLeft: DcMotor
    private lateinit var frontRight: DcMotor
    private lateinit var backRight: DcMotor

    override fun init() {
        frontLeft = hardwareMap.dcMotor["frontLeft"]
        backLeft = hardwareMap.dcMotor["backLeft"]
        frontRight = hardwareMap.dcMotor["frontRight"]
        backRight = hardwareMap.dcMotor["backRight"]
    }
// hey bbg
    override fun loop() {
        val angle = atan2(gamepad1.left_stick_y, gamepad1.left_stick_x)
        val magnitude = hypot(gamepad1.left_stick_x, gamepad1.left_stick_y)
        val turn = gamepad1.right_stick_x

        frontLeft.power = sin(angle + 0.25 * Math.PI) * magnitude + turn
        backRight.power = sin(angle + 0.25 * Math.PI) * magnitude + turn
        frontRight.power = -sin(angle - 0.25 * Math.PI) * magnitude + turn
        backLeft.power = -sin(angle - 0.25 * Math.PI) * magnitude + turn

        val factor = listOf(abs(frontRight.power), abs(frontLeft.power), abs(backRight.power), abs(backLeft.power))
    }

}

// [1, 2, 3, 4, 5]