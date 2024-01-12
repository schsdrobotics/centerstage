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
import org.firstinspires.ftc.teamcode.hardware.subsystem.Spatula.Companion.State.ALIGN
import org.firstinspires.ftc.teamcode.hardware.subsystem.Spatula.Companion.State.DOWN
import org.firstinspires.ftc.teamcode.hardware.subsystem.Spatula.Companion.State.UP
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.bindings.Binding
import java.util.function.DoubleSupplier


@TeleOp(group = "!")
class DriverControlled : OpModeEX() {
    private val gamepad by lazy { gamepadEX1() }
    private val gamepad2 by lazy { gamepadEX2() }

    private lateinit var lift: Lift
    private lateinit var drive: Drive
    private lateinit var intake: Intake
    private lateinit var spatula: Spatula
    private lateinit var puncher: Puncher


    override fun registerSubsystems() {
        drive = Drive(this, gamepad.leftX().invert(), gamepad.leftY().invert(), gamepad.rightX().invert())
        intake = Intake(this)
        spatula = Spatula(this)
        puncher = Puncher(this)
        lift = Lift(this, spatula)
    }

    override fun initEX() {
//        drive.feburary.init()
    }

    override fun registerBindings() {
        gamepad.square().onTrue(lift.to(LOW))
        gamepad.triangle().onTrue(lift.to(MID))
        gamepad.circle().onTrue(lift.to(HIGH))
        gamepad.cross().onTrue(lift.to(ZERO))
//
        gamepad.guide().onTrue(drive.reset())
//        gamepad.left_stick_button().whileTrue(drive.align())

        listOf(Pair(gamepad.left_trigger(), intake::reverse), Pair(gamepad.right_trigger(), intake::forward)).forEach {
            it.first.buildBinding()
                    .greaterThanEqualTo(0.3)
                    .lessThanEqualTo(0.6)
                    .bind()
                    .whileTrue(it.second(true))

            it.first.buildBinding()
                    .greaterThan(0.6)
                    .bind()
                    .whileTrue(it.second(false))
        }
//
//        val rightTriggerZeroed = gamepad.right_trigger().buildBinding().lessThan(0.3).bind() as Binding<*>
//        val leftTriggerZeroed = gamepad.left_trigger().buildBinding().lessThan(0.3).bind() as Binding<*>
//
//        rightTriggerZeroed.and(leftTriggerZeroed).onTrue(intake.stop())

        gamepad.dpad_left().onTrue(puncher.to(NONE))
        gamepad.dpad_up().onTrue(puncher.to(ONE))
        gamepad.dpad_right().onTrue(puncher.to(TWO))
//
        gamepad.dpad_down().onTrue(spatula.to(ALIGN))
        gamepad.right_bumper().onTrue(spatula.to(DOWN))
        gamepad.left_bumper().onTrue(spatula.to(UP))
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