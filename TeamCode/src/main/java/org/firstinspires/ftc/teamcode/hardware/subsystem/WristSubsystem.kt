package org.firstinspires.ftc.teamcode.hardware.subsystem

import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.hardware.AxonServo
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem
import org.firstinspires.ftc.teamcode.hardware.subsystem.WristSubsystem.Position.*
import kotlin.Double.Companion.NaN
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.sign
import kotlin.properties.Delegates

class WristSubsystem(val opmode: OpModeEX, val lift: LiftSubsystem) : Subsystem(opmode) {
    private val hw = opmode.hardwareMap

    private val wrist by lazy { AxonServo(hw, "wrist") }

    private val watchdog = ElapsedTime() // for timed corrections
    private var lock = false // to prevent super-travel (explained below)

    // prep on change of state
    private var state by Delegates.observable(Idle) { _, old, new -> if (old != new) prep() }

    // unlock automatically
    private var stage by Delegates.observable(0.0) { _, _, new -> if (new == STAGES) lock = false }

    private fun prep() {
        watchdog.reset()
        stage = 0.0
        lock = true
    }

    override fun init() {
        wrist.scaleRange(Restore.position, Deposit.position)
    }

    fun restore() = LambdaCommand()
            .setInterruptible(true)
            .setRequirements(this)
            // prevent restoration if the lift is at 0
            // TODO: introduce a threshold instead
            .setExecute { if (!lock && lift.current >= 50 ) state = Restore }
            .setFinish { false }

    fun deposit() = LambdaCommand()
            .setInterruptible(true)
            .setRequirements(this)
            .setExecute { if (!lock) state = Deposit }
            .setFinish { false }

    /**
     * here lies the mechanism behind "slowed" servos
     * the movement from A to B is broken into STAGES stages
     *
     * the "regulator" exists in the form of a watchdog timer
     * every "bucket" of time in the watchdog, the servo is moved an INTERVAL
     *
     * "super-travel" is when the stages are reset while the servo is
     * in-between A to B, i.e. stage !in listOf(0, STAGES)
     * a simple lock exists to prevent state reassignment
     *
     * this mechanism exists in the first place to reduce shock on the servo
     * more refinement required in order to achieve
     *
     * TODO: implement motion profiling
     *
     * this is budget motion profiling
     */
    override fun periodic() {
        opmode.telemetry.addData("stage", stage)
        opmode.telemetry.addData("state", state)
        opmode.telemetry.addData("lock", lock)

        if (state == Idle || stage == STAGES) return

        opmode.telemetry.addData("watchdog", watchdog.milliseconds())
        opmode.telemetry.addData("burden", stage * TIME_PER_STAGE)

        // TODO: test under looptime burden
        if (watchdog.milliseconds() >= stage * TIME_PER_STAGE) {
            stage++

            // TODO: is abs required here?
            val position = abs(state.factor + (Position.INTERVAL * stage))
            val delta = (Position.INTERVAL * (STAGES / 2))

            opmode.telemetry.addData("this is what you want", position)
            opmode.telemetry.addData("delta", delta)
            opmode.telemetry.addData("position 1", wrist.position)
            opmode.telemetry.addData("!!this", abs((wrist.position / 360.0) - position))


            if (abs((wrist.position / 360.0) - position) / (Position.INTERVAL) >= delta) {
                state = Position.invert(state)
                return
            }

            wrist.position = position
            opmode.telemetry.addData("position 2", wrist.position)
        }
    }


    override fun defaultCommandExecute() = restore().execute()

    override fun close() {}

    enum class Position(val position: Double, val factor: Int) {
        Idle(NaN, Int.MIN_VALUE),

        Restore(0.0, -1),
        Deposit(0.35, 0);

        companion object {
            fun invert(position: Position) = when (position) {
                Idle -> Idle
                Restore -> Deposit
                Deposit -> Restore
            }

            const val INTERVAL = 1.0 / STAGES // stage ^ -1
        }
    }

    // these properties allow for controlled servo movement
    // TODO: they probably tank loop times so like probably don't do that
    companion object {
        private const val STAGES = 75.0 // stages
        private const val TRANSITION_TIME = 500.0 // ms
        private const val TIME_PER_STAGE = TRANSITION_TIME / STAGES // stages / ms
    }
}