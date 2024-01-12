package org.firstinspires.ftc.teamcode.test.motor


import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.hardware.profile.AsymmetricMotionProfile
import org.firstinspires.ftc.teamcode.hardware.profile.ProfileConstraints

@TeleOp
class MotionProfileTest : OpMode() {
    private val left by lazy { hardwareMap["leftLift"] as DcMotorEx }
    private val right by lazy { hardwareMap["rightLift"] as DcMotorEx }
    private val timer = ElapsedTime()

    override fun init() {
        left.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        right.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        left.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        right.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    override fun start() { timer.reset() }

    override fun loop() {
//        val state = profile.calculate(timer.milliseconds())
//
//        motor.targetPosition = state.x.toInt()
//        motor.mode = DcMotor.RunMode.RUN_TO_POSITION
//        motor.power = 0.5
//
//        telemetry.addData("x", state.x)
//        telemetry.addData("a", state.a)
//        telemetry.addData("v", state.v)
        telemetry.addData("left position", left.currentPosition)
        telemetry.addData("right position", right.currentPosition)
//        telemetry.addData("error", st ate.x - motor.currentPosition)
    }
}