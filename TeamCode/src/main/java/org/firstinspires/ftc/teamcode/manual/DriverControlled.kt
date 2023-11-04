package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.subsystem.DriveSubsystem
import org.firstinspires.ftc.teamcode.hardware.subsystem.LiftSubsystem
import org.firstinspires.ftc.teamcode.hardware.subsystem.LiftSubsystem.Position.*
import org.firstinspires.ftc.teamcode.hardware.subsystem.WristSubsystem
import org.mercurialftc.mercurialftc.scheduler.OpModeEX

@TeleOp(group = "!")
class DriverControlled : OpModeEX() {
    private val gamepad by lazy { gamepadEX1() }

    private lateinit var lift: LiftSubsystem
    private lateinit var drive: DriveSubsystem
    private lateinit var wrist: WristSubsystem

    override fun registerSubsystems() {
        lift = LiftSubsystem(this)
        drive = DriveSubsystem(this, gamepad.leftX(), gamepad.leftY(), gamepad.rightX())
        wrist = WristSubsystem(this)
    }

    override fun initEX() { }

    override fun registerBindings() {
        gamepad.square().onTrue(lift.to(LOW))
        gamepad.triangle().onTrue(lift.to(MID))
        gamepad.circle().onTrue(lift.to(HIGH))
        gamepad.x().onTrue(lift.to(ZERO))

        gamepad.guide().onTrue(drive.reset())

        gamepad.left_bumper().onTrue(wrist.restore())
        gamepad.right_bumper().onTrue(wrist.deposit())
    }

    override fun init_loopEX() {}
    override fun startEX() {}
    override fun loopEX() {}
    override fun stopEX() {}
}