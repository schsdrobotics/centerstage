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
class DriverControlled : OpModeEX() {
    private val gamepad by lazy { gamepadEX1() }
    private val gamepad2 by lazy { gamepadEX2() }

//    private lateinit var lift: Lift
    private lateinit var drive: Drive
//    private lateinit var intake: Intake
//    private lateinit var spatula: Spatula
//    private lateinit var puncher: Puncher

    override fun registerSubsystems() {
//        lift = Lift(this)
        drive = Drive(this, gamepad.leftX().invert(), gamepad.leftY().invert(), gamepad.rightX().invert())
//        intake = Intake(this)
//        spatula = Spatula(this)
//        puncher = Puncher(this)
    }

    override fun initEX() {
//        drive.feburary.init()
    }

    override fun registerBindings() {
//        gamepad.square().onTrue(lift.to(LOW))
//        gamepad.triangle().onTrue(lift.to(MID))
//        gamepad.circle().onTrue(lift.to(HIGH))
//        gamepad.cross().onTrue(lift.to(ZERO))
//
        gamepad.guide().onTrue(drive.reset())
//        gamepad.left_stick_button().whileTrue(drive.align())
//
//        gamepad.dpad_left().onTrue(puncher.to(NONE))
//        gamepad.dpad_up().onTrue(puncher.to(ONE))
//        gamepad.dpad_right().onTrue(puncher.to(TWO))
//
//        gamepad.dpad_left().onTrue(spatula.to(DOWN))
//        gamepad.dpad_right().onTrue(spatula.to(UP))
    }

    override fun init_loopEX() {}
    override fun startEX() {
        telemetry.msTransmissionInterval = 20
    }

    override fun loopEX() {
        drive.feburary.test()

        telemetry.addData("current", hardwareMap.getAll(LynxModule::class.java).fold(0.0) { acc, it -> acc + it.getCurrent(CurrentUnit.AMPS) })
    }
    override fun stopEX() {}
}