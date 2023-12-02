package org.firstinspires.ftc.teamcode.test

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.library.util.profile

@TeleOp
class MotionProfileTest : OpMode() {
    private val motor by lazy { hardwareMap["motor"] as DcMotorEx }

    override fun init() {
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    override fun loop() {
        motor.power = profile(motor.currentPosition, 3000)

        telemetry.addData("power", motor.power)
        telemetry.addData("position", motor.currentPosition)
    }
}