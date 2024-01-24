package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.hardware.rev.Rev2mDistanceSensor
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher.State.*

class Puncher(val hw: HardwareMap, val telemetry: Telemetry, var state: State = NONE) : SubsystemBase() {
    private val pacer by lazy { hw["distance"] as Rev2mDistanceSensor }

    private val puncher by lazy {
        val it = hw["puncher"] as Servo
        it.direction = Servo.Direction.REVERSE
        it
    }

    init { this.puncher.position = state.position }

    var distance = 0.0

    fun update() { distance = pacer.getDistance(DistanceUnit.CM) }

    fun to(state: State) { this.state = state }

    fun next() { this.state = when (this.state) { TWO -> ONE; ONE -> NONE; NONE -> TWO } }

    override fun periodic() {
        puncher.position = state.position

        telemetry.addData("puncher state", state.toString())
        telemetry.addData("puncher distance", distance)
    }

    enum class State(val position: Double) {
        TWO(1.0),
        ONE(0.25),
        NONE(0.0),
    }
}