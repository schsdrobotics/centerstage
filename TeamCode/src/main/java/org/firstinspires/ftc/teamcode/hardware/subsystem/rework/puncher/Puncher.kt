package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.hardware.Robot.PuncherHardware.servo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.EfficientSubsystem
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher.State.*

class Puncher(var state: State = NONE, val telemetry: Telemetry? = null) : EfficientSubsystem() {
    init { servo.position = state.position }

    fun next() { state = when (state) { TWO -> ONE; ONE -> NONE; NONE -> TWO } }
    fun to(state: State) {
        this.state = state
        servo.position = state.position
    }

    override fun periodic() {
        if (telemetry != null) {
            telemetry.addData("puncher state", state)
            telemetry.addData("puncher pos", servo.position)
        }
    }

    override fun read() { }
    override fun write() { servo.position = state.position }

    override fun reset() { servo.position = NONE.position }

    enum class State(val position: Double) {
        TWO(0.5),
        ONE(0.25),
        NONE(0.11),
    }
}