package org.firstinspires.ftc.teamcode.test

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.hardware.profile.AsymmetricMotionProfile
import org.firstinspires.ftc.teamcode.hardware.profile.Constraints
import org.firstinspires.ftc.teamcode.library.util.profile

@TeleOp
class MotionProfileTest : OpMode() {
    private val profile = AsymmetricMotionProfile(0.0, 20000.0, Constraints(1.75, 10.0))
    private val motor by lazy { hardwareMap["motor"] as DcMotorEx }
    private val timer = ElapsedTime()

    override fun init() {
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    override fun start() = timer.reset()

    override fun loop() {
        motor.power = profile.calculate(timer.seconds()).a

        telemetry.addData("power", motor.power)
        telemetry.addData("position", motor.currentPosition)
    }
}