package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial.Drive
import org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial.Intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial.Lift.Position.HIGH
import org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial.Lift.Position.LOW
import org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial.Lift.Position.MID
import org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial.Lift.Position.ZERO
import org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial.Puncher
import org.firstinspires.ftc.teamcode.hardware.subsystem.mercurial.Spatula
import org.mercurialftc.mercurialftc.scheduler.OpModeEX


@TeleOp(group = "!")
class OldDriverControlled : OpModeEX() {
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
        lift = Lift(this)
        puncher = Puncher(this)
    }

    override fun initEX() {
//        drive.feburary.init()
    }

    override fun registerBindings() {
        gamepad.square().onTrue(lift.to(LOW))
        gamepad.triangle().onTrue(lift.to(MID))
        gamepad.circle().onTrue(lift.to(HIGH))
        gamepad.cross().onTrue(lift.to(ZERO))

        gamepad.guide().onTrue(drive.reset())

        listOf(Pair(gamepad.left_trigger(), intake::reverse), Pair(gamepad.right_trigger(), intake::forward)).forEach {
            it.first.buildBinding()
                    .greaterThanEqualTo(0.0)
                    .lessThanEqualTo(0.3)
                    .bind()
                    .whileTrue(intake.stop())

            it.first.buildBinding()
                    .greaterThan(0.3)
                    .lessThan(0.6)
                    .bind()
                    .whileTrue(it.second(true))

            it.first.buildBinding()
                    .greaterThanEqualTo(0.6)
                    .bind()
                    .whileTrue(it.second(false))
        }


        gamepad.dpad_up().onTrue(puncher.next())
        gamepad.dpad_left().onTrue(intake.up())
        gamepad.dpad_right().onTrue(intake.down())
        gamepad.dpad_down().onTrue(lift.bang())

        gamepad.left_bumper().onTrue(spatula.score())
        gamepad.right_bumper().onTrue(spatula.transfer())
    }

    override fun init_loopEX() {}
    override fun startEX() {
        telemetry.msTransmissionInterval = 20
    }

    override fun loopEX() {
//        drive.feburary.test()

        telemetry.addData("current", hardwareMap.getAll(LynxModule::class.java).fold(0.0) { acc, it -> acc + it.getCurrent(CurrentUnit.AMPS) })
    }
    override fun stopEX() {}
}