package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.library.LoopTimer
import kotlin.math.sin

@TeleOp
class LoopTime : OpMode() {
    private val motors by lazy { List(4) { hardwareMap["motor$it"] as DcMotorEx } }
    private val timer = LoopTimer()

    override fun init() { }

    override fun start() = timer.reset()

    override fun loop() {
        motors.forEach { it.power = 0.5 }

        telemetry.addData("last time", timer.times.last())
        telemetry.addData("average", timer.average())

        timer.record()
    }
}