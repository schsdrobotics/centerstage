package org.firstinspires.ftc.teamcode.test

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.ServoImplEx
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.hardware.profile.AsymmetricMotionProfile
import org.firstinspires.ftc.teamcode.hardware.profile.Constraints

@TeleOp
class ServoTest : OpMode() {
    val servo by lazy { hardwareMap["servo"] as ServoImplEx }
    val timer = ElapsedTime()
    val profile = AsymmetricMotionProfile(1.0, 0.25, Constraints(0.15, 0.15, 0.05))

    override fun init() {
        servo.position = 1.0
        timer.reset()
        profile
    }

    override fun loop() {
        val p = profile.calculate(timer.seconds())
        servo.position = p.x

        telemetry.addData("p", p.x)
        telemetry.addData("pos", servo.position)
    }
}