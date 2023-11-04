package org.firstinspires.ftc.teamcode.hardware.subsystem

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.bindings.gamepadex.DomainSupplier
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem


class DriveSubsystem(val opmode: OpModeEX, val x: DomainSupplier, val y: DomainSupplier, val z: DomainSupplier) : Subsystem(opmode) {
    val hw = opmode.hardwareMap

    private val frontLeft by lazy { Motor(hw, "frontLeft") }
    private val frontRight by lazy { Motor(hw, "frontRight") }
    private val backLeft by lazy { Motor(hw, "backLeft") }
    private val backRight by lazy { Motor(hw, "backRight") }

    private val drive by lazy { MecanumDrive(frontLeft, frontRight, backLeft, backRight) }

    private val imu by lazy { hw["imu"] as IMU }

    override fun init() {
        frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)

        drive

        imu.initialize(
                IMU.Parameters(
                        RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                                RevHubOrientationOnRobot.UsbFacingDirection.UP
                        )
                )
        )


        defaultCommand = move()
    }

    fun move() = LambdaCommand()
            .setRequirements(this)
            .setRunStates(OpModeEX.OpModeEXRunStates.LOOP)
            .setExecute { defaultCommandExecute() }
            .setFinish { false }

    fun reset() = LambdaCommand()
            .setRequirements(this)
            .setRunStates(OpModeEX.OpModeEXRunStates.LOOP)
            .setExecute { imu.resetYaw() }
            .setFinish { true }

    override fun periodic() {}

    override fun defaultCommandExecute() {
        drive.driveFieldCentric(
                -x.asDouble,
                -y.asDouble,
                -z.asDouble,
                imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES),
                true,
        )
    }

    override fun close() {}
}