package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import kotlin.math.sin

@TeleOp
class Led : OpMode() {
    private val led: DcMotor by lazy { hardwareMap.dcMotor["led"] }

    override fun init() { led }

    override fun loop() {
        led.power = ((sin(SPEED * runtime) + 1) / 2) + MINIMUM
    }

    companion object {
        const val SPEED = 2
        const val MINIMUM = 0.0000325
    }
}