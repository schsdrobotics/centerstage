package org.firstinspires.ftc.teamcode.util.extensions

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit

val OpMode.currentDraw: Double
    get() = hardwareMap.getAll(LynxModule::class.java).fold(0.0) { acc, it -> acc + it.getCurrent(CurrentUnit.AMPS) }