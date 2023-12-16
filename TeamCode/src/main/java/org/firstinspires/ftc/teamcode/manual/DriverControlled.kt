package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.hardware.subsystem.Deposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.Drive
import org.firstinspires.ftc.teamcode.hardware.subsystem.Intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.Lift.Position.HIGH
import org.firstinspires.ftc.teamcode.hardware.subsystem.Lift.Position.LOW
import org.firstinspires.ftc.teamcode.hardware.subsystem.Lift.Position.MID
import org.firstinspires.ftc.teamcode.hardware.subsystem.Lift.Position.ZERO
import org.firstinspires.ftc.teamcode.hardware.subsystem.Wrist
import org.firstinspires.ftc.teamcode.util.inParallel
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.commands.Command
import org.mercurialftc.mercurialftc.scheduler.commands.ParallelCommandGroup


@TeleOp(group = "!")
class DriverControlled : OpModeEX() {
    private val gamepad by lazy { gamepadEX1() }

    private lateinit var lift: Lift
    private lateinit var drive: Drive
    private lateinit var wrist: Wrist
    private lateinit var intake: Intake
    private lateinit var deposit: Deposit

    override fun registerSubsystems() {
        lift = Lift(this)
        drive = Drive(this, gamepad.leftX().invert(), gamepad.leftY().invert(), gamepad.rightX().invert())
        wrist = Wrist(this, lift)
        intake = Intake(this)
        deposit = Deposit(this)
    }

    override fun initEX() {
        drive.feburary.init()
    }

    override fun registerBindings() {
        gamepad.square().onTrue(lift.to(LOW))
        gamepad.triangle().onTrue(lift.to(MID))
        gamepad.circle().onTrue(lift.to(HIGH))
        gamepad.cross().onTrue(lift.to(ZERO))

        gamepad.guide().onTrue(drive.reset())

        gamepad.dpad_up().onTrue(inParallel(intake.spin(), deposit.spin()))
        gamepad.right_stick_button().onTrue(inParallel(intake.fast(), deposit.spin()))
        gamepad.dpad_left().onTrue(inParallel(intake.stop(), deposit.stop()))
        gamepad.dpad_down().onTrue(inParallel(intake.reverse(), deposit.reverse()))

        gamepad.left_bumper().onTrue(wrist.restore())
        gamepad.right_bumper().onTrue(wrist.deposit())

        gamepad.share().onTrue(deposit.shut())
        gamepad.options().onTrue(deposit.open())
    }

    override fun init_loopEX() {}
    override fun startEX() {}
    override fun loopEX() {
        var current = 0.0

        val allHubs = hardwareMap.getAll(LynxModule::class.java)

        hardwareMap.getAll(LynxModule::class.java).fold(0.0) { acc, it -> acc + it.getCurrent(CurrentUnit.AMPS) }

        for (hub in allHubs) {
            current += hub.getCurrent(CurrentUnit.AMPS)
        }

        drive.feburary.test()

        telemetry.addData("current", current)
    }
    override fun stopEX() {}
}