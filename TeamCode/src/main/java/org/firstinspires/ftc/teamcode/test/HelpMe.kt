package org.firstinspires.ftc.teamcode.test

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.DriveSubsystem
import org.firstinspires.ftc.teamcode.hardware.LiftSubsystem
import org.firstinspires.ftc.teamcode.hardware.LiftSubsystem.Position.*
import org.mercurialftc.mercurialftc.scheduler.OpModeEX

@TeleOp
class HelpMe : OpModeEX() {
    private val gamepad by lazy { gamepadEX1() }

    private lateinit var lift: LiftSubsystem
    private lateinit var drive: DriveSubsystem

    override fun registerSubsystems() {
        lift = LiftSubsystem(this)
        drive = DriveSubsystem(this, gamepad.leftX(), gamepad.leftY(), gamepad.rightX())
    }

    override fun initEX() { }

    override fun registerBindings() {
        gamepad.square().onTrue(lift.to(LOW))
        gamepad.triangle().onTrue(lift.to(MID))
        gamepad.circle().onTrue(lift.to(HIGH))
        gamepad.x().onTrue(lift.to(ZERO))

        gamepad.guide().onTrue(drive.reset())
    }

    override fun init_loopEX() {}
    override fun startEX() {}
    override fun loopEX() {}
    override fun stopEX() {}
}