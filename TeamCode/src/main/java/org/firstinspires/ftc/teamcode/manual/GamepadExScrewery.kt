package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.library.GamepadEx
import org.firstinspires.ftc.teamcode.library.GamepadEx.Button.A

@TeleOp
class GamepadExScrewery : OpMode() {
    private val gamepad by lazy { GamepadEx(gamepad1) }

    override fun init() { gamepad }

    override fun loop() {
        telemetry.addData("wow", gamepad.pressed(A))
    }
}