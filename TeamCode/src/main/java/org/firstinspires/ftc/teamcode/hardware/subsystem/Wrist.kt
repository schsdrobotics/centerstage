package org.firstinspires.ftc.teamcode.hardware.subsystem

import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.hardware.profile.AsymmetricMotionProfile
import org.firstinspires.ftc.teamcode.hardware.axon.AxonServo
import org.firstinspires.ftc.teamcode.hardware.profile.ProfileConstraints
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem
import org.firstinspires.ftc.teamcode.hardware.subsystem.Wrist.Position.*
import kotlin.properties.Delegates

class Wrist(val opmode: OpModeEX, val lift: Lift) : Subsystem(opmode) {
    private val hw = opmode.hardwareMap

    private val wrist by lazy { AxonServo(hw, "wrist") }

    private val watchdog = ElapsedTime() // for timed corrections

    // prep on change of state
    private var state by Delegates.observable(Restore) { _, old, new -> if (old != new) prep() }

    private var profile = generate()

    private var locked = false

    private fun generate() = AsymmetricMotionProfile(wrist.position, state.position, constraint)

    private fun prep() {
        watchdog.reset()
        profile = generate()
    }

    override fun init() { prep() }

    fun restore() = LambdaCommand()
            .setInterruptible(true)
            .setRequirements(this)
            // prevent restoration if the lift is at 0
            // TODO: introduce a threshold instead
            .setExecute { if (!locked && lift.current > 10) state = Restore; }
            .setFinish { false }

    fun deposit() = LambdaCommand()
            .setInterruptible(true)
            .setRequirements(this)
            .setExecute { if (!locked) state = Position.Deposit; }
            .setFinish { false }

    override fun periodic() {
        val position = profile.calculate(watchdog.seconds()).x

        opmode.telemetry.addData("current position", wrist.position)
        opmode.telemetry.addData("calculated position", position)
        opmode.telemetry.addData("state", state)

        if (lift.current > 10) wrist.position = position
    }

    override fun defaultCommandExecute() = restore().execute()

    override fun close() {}

    enum class Position(val position: Double) {
        Restore(0.0),
        Deposit(0.35)
    }

    companion object {
        private val constraint = ProfileConstraints(5.0, 0.125, 0.05)
    }
}