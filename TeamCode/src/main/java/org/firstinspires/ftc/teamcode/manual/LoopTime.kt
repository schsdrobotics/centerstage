package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.hardware.Drivetrain
import org.firstinspires.ftc.teamcode.library.LoopTimer

@TeleOp
class LoopTime : OpMode() {
    private val drivetrain by lazy { Drivetrain(this) }

    private val timer = LoopTimer()

    override fun init() { drivetrain }

    override fun start() = timer.reset()

    override fun loop() {
        val average = timer.average()

        drivetrain.loop()

        telemetry.addData("last time", "${timer.times.last()}ms")
        telemetry.addData("average", "${average}ms")
        telemetry.addData("hz", "${1000.0 / average} Hz")

        timer.record()
    }
}