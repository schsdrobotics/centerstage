package org.firstinspires.ftc.teamcode.hardware.subsystem

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.library.Feburary
import org.firstinspires.ftc.teamcode.library.Vector3
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import org.mercurialftc.mercurialftc.scheduler.bindings.gamepadex.DomainSupplier
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import org.mercurialftc.mercurialftc.scheduler.subsystems.Subsystem


class DriveSubsystem(val opmode: OpModeEX, val givenX: DomainSupplier, val givenY: DomainSupplier, val givenZ: DomainSupplier) : Subsystem(opmode) {
    val hw = opmode.hardwareMap

    private val frontLeft by lazy { Motor(hw, "frontLeft") }
    private val frontRight by lazy { Motor(hw, "frontRight") }
    private val backLeft by lazy { Motor(hw, "backLeft") }
    private val backRight by lazy { Motor(hw, "backRight") }

    private val drive by lazy { MecanumDrive(frontLeft, frontRight, backLeft, backRight) }

    private val imu by lazy { hw["imu"] as IMU }

    val feburary by lazy { Feburary(hw, opmode) }

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

    fun align() = LambdaCommand()
            .setInterruptible(false)
            .setRequirements(this)
            .setRunStates(OpModeEX.OpModeEXRunStates.LOOP)
            .setExecute { defaultCommandExecute(feburary.feed()) }
            .setFinish { false }

    fun move(vec: Vector3) = move(vec.x, vec.y, vec.z)

    fun move() = LambdaCommand()
            .setInterruptible(true)
            .setRequirements(this)
            .setRunStates(OpModeEX.OpModeEXRunStates.LOOP)
            .setExecute { defaultCommandExecute(givenX.asDouble, givenY.asDouble, givenZ.asDouble) }
            .setFinish { false }

    fun move(x: Double, y: Double, z: Double) = LambdaCommand()
            .setInterruptible(true)
            .setRequirements(this)
            .setRunStates(OpModeEX.OpModeEXRunStates.LOOP)
            .setExecute { defaultCommandExecute(x, y, z) }
            .setFinish { false }

    fun reset() = LambdaCommand()
            .setRequirements(this)
            .setRunStates(OpModeEX.OpModeEXRunStates.LOOP)
            .setExecute { imu.resetYaw() }
            .setFinish { true }

    override fun periodic() {}

    fun defaultCommandExecute(vec: Vector3) = defaultCommandExecute(-vec.x, -vec.y, -vec.z)

    fun defaultCommandExecute(x: Double, y: Double, z: Double) {
        opmode.telemetry.addLine("defaultCommandExecute x y z")
        opmode.telemetry.addData("x", x)
        opmode.telemetry.addData("x", y)
        opmode.telemetry.addData("x", z)
        opmode.telemetry.addLine("---------------------------")

        drive.driveFieldCentric(x, y, z,
                imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES),
                true,
        )
    }

    override fun defaultCommandExecute() {
        opmode.telemetry.addLine("defaultCommandExecute given")
        opmode.telemetry.addData("x", givenX.asDouble)
        opmode.telemetry.addData("x", givenY.asDouble)
        opmode.telemetry.addData("x", givenZ.asDouble)
        opmode.telemetry.addLine("---------------------------")

        drive.driveFieldCentric(
                givenX.asDouble,
                givenY.asDouble,
                givenZ.asDouble,
                imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES),
                true,
        )
    }

    override fun close() {}
}