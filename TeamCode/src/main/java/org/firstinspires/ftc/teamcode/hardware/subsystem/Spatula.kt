package org.firstinspires.ftc.teamcode.hardware.subsystem

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.hardware.subsystem.Spatula.Companion.State.DOWN
import org.firstinspires.ftc.teamcode.hardware.subsystem.Spatula.Companion.State.UP
import org.firstinspires.ftc.teamcode.hardware.subsystem.Spatula.Companion.State.ALIGN
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem

class Spatula(opmode: OpModeEX) : Subsystem(opmode) {
    private val hw = opmode.hardwareMap
    private val spatula by lazy { hw["spatula"] as Servo }

    private var state = DOWN

    override fun init() {
        spatula.scaleRange(ZERO, ONE)
        spatula.direction = Servo.Direction.REVERSE
    }

    fun score() = LambdaCommand()
            .setRequirements(this)
            .setExecute { state = UP }
            .setFinish { false }

    fun transfer() = LambdaCommand()
            .setRequirements(this)
            .setExecute { state = DOWN }
            .setFinish { false }

    fun align() = LambdaCommand()
            .setRequirements(this)
            .setExecute { state = ALIGN }
            .setFinish { false }

    fun to(state: State) = LambdaCommand()
            .setRequirements(this)
            .setExecute { this.state = state }
            .setFinish { false }

    override fun periodic() { spatula.position = state.position }

    override fun defaultCommandExecute() = Unit

    override fun close() { spatula.position = DOWN.position }

    companion object {
        enum class State(val position: Double) {
            DOWN(0.15),
            ALIGN(0.1),
            UP(0.89),
        }

        private const val ZERO = 0.3
        private const val ONE  = 0.6
    }
}