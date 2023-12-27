package org.firstinspires.ftc.teamcode.test.motor

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import kotlin.math.max

@TeleOp
class MotorCurrent : OpMode() {
    private val motor by lazy { hardwareMap.dcMotor["motor"] as DcMotorEx }

    override fun init() { motor }
    override fun loop() {
        val halved = (gamepad1.right_trigger.toDouble()) / 2.0
        val full = (gamepad1.left_trigger.toDouble())

        motor.power = max(halved, full)

        telemetry.addData("power", motor.power * 100.0)
        telemetry.addData("current (mA)", motor.getCurrent(CurrentUnit.MILLIAMPS))
    }
}