package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.LoopTimer

@TeleOp
class LoopTime : OpMode() {
    private val timer = LoopTimer()

    override fun init() { }

    override fun start() = timer.reset()

    override fun loop() {
        val average = timer.average()

        telemetry.addData("last time", "${timer.times.last()}ms")
        telemetry.addData("average", "${average}ms")
        telemetry.addData("hz", "${1000.0 / average} Hz")

        timer.record()
    }
}