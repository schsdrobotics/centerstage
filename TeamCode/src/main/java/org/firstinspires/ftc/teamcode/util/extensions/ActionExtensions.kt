package org.firstinspires.ftc.teamcode.util.extensions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action

val Action.isFinished
    get() = !this.run(TelemetryPacket())