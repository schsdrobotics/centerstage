package org.firstinspires.ftc.teamcode.hardware.subsystem

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.hardware.subsystem.Puncher.Companion.State.NONE
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem

class Puncher(opmode: OpModeEX) : Subsystem(opmode) {
    private val hw = opmode.hardwareMap
    private val puncher by lazy { hw["puncher"] as Servo }

    private var state = NONE

    override fun init() {
//        puncher.direction = Servo.Direction.REVERSE
    }

    fun to(state: State) = LambdaCommand()
            .setRequirements(this)
            .setExecute { this.state = state }
            .setFinish { false }

    override fun periodic() { puncher.position = state.position }

    override fun defaultCommandExecute() = Unit

    override fun close() { puncher.position = NONE.position }

    companion object {
        enum class State(val position: Double) {
            NONE(0.0),
            ONE(0.47),
            TWO(1.0)
        }
    }
}