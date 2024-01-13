package org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial.Puncher.Companion.State.*
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem

class Puncher(val opmode: OpModeEX) : Subsystem(opmode) {
    private val hw = opmode.hardwareMap
    private val puncher by lazy { hw["puncher"] as Servo }

    private var state = NONE
    private var count = 0

    override fun init() {  }

    fun to(state: State) = LambdaCommand()
            .setRequirements(this)
            .setExecute { this.state = state }
            .setFinish { false }

    fun next() = LambdaCommand()
            .setRequirements(this)
            .setExecute { this.state = State.values()[count % 3].also { count++ } }
            .setFinish { true }

    override fun periodic() {
        puncher.position = state.position
        opmode.telemetry.addData("count", count)
    }

    override fun defaultCommandExecute() = Unit

    override fun close() {
        puncher.position = NONE.position
    }

    companion object {
        enum class State(val position: Double) {
            TWO(0.05),
            ONE(0.8),
            NONE(1.0)
        }
    }
}