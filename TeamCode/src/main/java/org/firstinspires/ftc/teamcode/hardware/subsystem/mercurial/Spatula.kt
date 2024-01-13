package org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial.Spatula.Companion.State.DOWN
import org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial.Spatula.Companion.State.UP
import org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial.Spatula.Companion.State.ALIGN
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem
import kotlin.math.sin

class Spatula(val opmode: OpModeEX) : Subsystem(opmode) {
    private val hw = opmode.hardwareMap
    private val spatula by lazy { hw["spatula"] as Servo }

    private var state = DOWN
    private var ticks = 0

    override fun init() {
        spatula.scaleRange(ZERO, ONE)
        spatula.direction = Servo.Direction.REVERSE
        spatula.position = DOWN.position

        opmode.telemetry.addLine("inited")
    }

    fun score() = LambdaCommand()
            .setRequirements(this)
            .setExecute { state = UP }
            .setFinish { true }

    fun transfer() = LambdaCommand()
            .setRequirements(this)
            .setExecute { state = DOWN }
            .setFinish { true }

    fun align() = LambdaCommand()
            .setRequirements(this)
            .setExecute { state = ALIGN }
            .setFinish { true }

    fun to(state: State) = LambdaCommand()
            .setRequirements(this)
            .setExecute { this.state = state }
            .setFinish { false }

    fun liftAt(ticks: Int) { this.ticks = ticks }

    override fun periodic() {
//        if (state == UP && ticks <= 50) return
//        if (ticks in 20..100) {
//            spatula.position = ALIGN.position
//            return
//        }

        spatula.position = state.position

        opmode.telemetry.addData("sin", sin(opmode.runtime))

        opmode.telemetry.addData("state", state)
        opmode.telemetry.addData("state pos", state.position)
        opmode.telemetry.addData("spatula pos", spatula.position)
    }

    override fun defaultCommandExecute() = Unit

    override fun close() { spatula.position = DOWN.position }

    companion object {
        enum class State(val position: Double) {
            ALIGN(0.0),
            DOWN(0.412), // 0.142
            UP(0.75),
        }

        private const val ZERO = 0.0
        private const val ONE  = 1.0
    }
}