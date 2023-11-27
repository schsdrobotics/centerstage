package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class ControllerLights : OpMode() {
    override fun init() { }

    override fun loop() {
        gamepad1.setLedColor(gamepad1.left_trigger.toDouble(), gamepad1.right_trigger.toDouble(), gamepad1.left_stick_x.toDouble(), 250)
    }
}