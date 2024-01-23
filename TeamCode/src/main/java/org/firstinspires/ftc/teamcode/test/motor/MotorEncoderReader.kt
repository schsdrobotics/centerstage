package org.firstinspires.ftc.teamcode.test.motor

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx

@TeleOp
class MotorEncoderReader : OpMode() {
    val motor by lazy { hardwareMap["leftLift"] as DcMotorEx }

    override fun init() {
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    }

    override fun loop() {
        telemetry.addData("position", motor.currentPosition)
    }
}