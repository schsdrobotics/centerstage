package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.hardware.subsystem.Drive
import org.firstinspires.ftc.teamcode.hardware.subsystem.Intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.Lift.Position.HIGH
import org.firstinspires.ftc.teamcode.hardware.subsystem.Lift.Position.LOW
import org.firstinspires.ftc.teamcode.hardware.subsystem.Lift.Position.MID
import org.firstinspires.ftc.teamcode.hardware.subsystem.Lift.Position.ZERO
import org.firstinspires.ftc.teamcode.hardware.subsystem.Puncher
import org.firstinspires.ftc.teamcode.hardware.subsystem.Puncher.Companion.State.NONE
import org.firstinspires.ftc.teamcode.hardware.subsystem.Puncher.Companion.State.ONE
import org.firstinspires.ftc.teamcode.hardware.subsystem.Puncher.Companion.State.TWO
import org.firstinspires.ftc.teamcode.hardware.subsystem.Spatula
import org.firstinspires.ftc.teamcode.hardware.subsystem.Spatula.Companion.State.DOWN
import org.firstinspires.ftc.teamcode.hardware.subsystem.Spatula.Companion.State.UP
import org.mercurialftc.mercurialftc.scheduler.OpModeEX


@TeleOp(group = "!")
class Prototype : OpModeEX() {
    private val gamepad by lazy { gamepadEX1() }

    private lateinit var spatula: Spatula
    private lateinit var puncher: Puncher

    override fun registerSubsystems() {
        spatula = Spatula(this)
        puncher = Puncher(this)
    }

    override fun initEX() { }

    override fun registerBindings() {
        gamepad.dpad_left().onTrue(puncher.to(NONE))
        gamepad.dpad_up().onTrue(puncher.to(ONE))
        gamepad.dpad_right().onTrue(puncher.to(TWO))

        gamepad.left_bumper().onTrue(spatula.to(DOWN))
        gamepad.right_bumper().onTrue(spatula.to(UP))
    }

    override fun init_loopEX() {}
    override fun startEX() {
        telemetry.msTransmissionInterval = 20
    }

    override fun loopEX() {
        telemetry.addData("current", hardwareMap.getAll(LynxModule::class.java).fold(0.0) { acc, it -> acc + it.getCurrent(CurrentUnit.AMPS) })
    }

    override fun stopEX() {}
}