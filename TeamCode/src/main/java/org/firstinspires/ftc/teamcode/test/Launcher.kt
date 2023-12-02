package org.firstinspires.ftc.teamcode.test

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp
class Launcher : OpMode() {
    private val motor: DcMotorEx by lazy { hardwareMap["intake"] as DcMotorEx }

    override fun init() {
        motor.direction = DcMotorSimple.Direction.REVERSE
    }

    override fun loop() {
        motor.power = gamepad1.right_trigger.toDouble()
    }
}