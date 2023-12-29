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
    private val profile = AsymmetricMotionProfile(0.0, 20000.0, ProfileConstraints(1.0, 0.05))
    private val motor by lazy { hardwareMap["motor"] as DcMotorEx }
    private val timer = ElapsedTime()

    override fun init() {
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    override fun start() {
        timer.reset()
    }

    override fun loop() {
        val state = profile.calculate(timer.milliseconds())

        motor.targetPosition = state.x.toInt()
        motor.mode = DcMotor.RunMode.RUN_TO_POSITION
        motor.power = 0.5

        telemetry.addData("x", state.x)
        telemetry.addData("a", state.a)
        telemetry.addData("v", state.v)
        telemetry.addData("position", motor.currentPosition)
        telemetry.addData("error", state.x - motor.currentPosition)
    }
}