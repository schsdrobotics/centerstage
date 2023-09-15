package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class toggle: OpMode(){
    private var isPressed = false
    private var previous = false

    override fun init() {

    }

    override fun loop() {
        if(gamepad1.a && !previous) {
            isPressed = !isPressed
        }

        previous = gamepad1.a


        telemetry.addData("isPressed ", isPressed)
    }
}