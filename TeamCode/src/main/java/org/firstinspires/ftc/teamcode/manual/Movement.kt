package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Drivetrain

@TeleOp
class Movement : OpMode() {
    private lateinit var drivetrain: Drivetrain

    override fun init() {
        drivetrain = Drivetrain(this)
    }

    override fun loop() {
        drivetrain.loop()
    }
}