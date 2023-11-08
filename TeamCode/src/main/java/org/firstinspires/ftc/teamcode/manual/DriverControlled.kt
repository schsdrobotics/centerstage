package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.hardware.subsystem.DriveSubsystem
import org.firstinspires.ftc.teamcode.hardware.subsystem.IntakeSubsystem
import org.firstinspires.ftc.teamcode.hardware.subsystem.LiftSubsystem
import org.firstinspires.ftc.teamcode.hardware.subsystem.LiftSubsystem.Position.HIGH
import org.firstinspires.ftc.teamcode.hardware.subsystem.LiftSubsystem.Position.LOW
import org.firstinspires.ftc.teamcode.hardware.subsystem.LiftSubsystem.Position.MID
import org.firstinspires.ftc.teamcode.hardware.subsystem.LiftSubsystem.Position.ZERO
import org.firstinspires.ftc.teamcode.hardware.subsystem.WristSubsystem
import org.mercurialftc.mercurialftc.scheduler.OpModeEX


@TeleOp(group = "!")
class DriverControlled : OpModeEX() {
    private val gamepad by lazy { gamepadEX1() }

    private lateinit var lift: LiftSubsystem
    private lateinit var drive: DriveSubsystem
    private lateinit var wrist: WristSubsystem
    private lateinit var intake: IntakeSubsystem

    override fun registerSubsystems() {
        lift = LiftSubsystem(this)
        drive = DriveSubsystem(this, gamepad.leftX(), gamepad.leftY(), gamepad.rightX())
        wrist = WristSubsystem(this)
        intake = IntakeSubsystem(this)
    }

    override fun initEX() { }

    override fun registerBindings() {
        gamepad.square().onTrue(lift.to(LOW))
        gamepad.triangle().onTrue(lift.to(MID))
        gamepad.circle().onTrue(lift.to(HIGH))
        gamepad.cross().onTrue(lift.to(ZERO))

        gamepad.guide().onTrue(drive.reset())

        gamepad.options().toggle(intake.spin())

        gamepad.left_bumper().onTrue(wrist.restore())
        gamepad.right_bumper().onTrue(wrist.deposit())
    }

    override fun init_loopEX() {}
    override fun startEX() {}
    override fun loopEX() {
        var current = 0.0

        val allHubs = hardwareMap.getAll(LynxModule::class.java)

        for (hub in allHubs) {
            current += hub.getCurrent(CurrentUnit.AMPS)
        }

        telemetry.addData("current", current)
    }
    override fun stopEX() {}
}