package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive

import com.arcrobotics.ftclib.command.SubsystemBase
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit

class Drive(val hw: HardwareMap) : SubsystemBase() {
    private val frontLeft by lazy { Motor(hw, "frontLeft") }
    private val frontRight by lazy { Motor(hw, "frontRight") }
    private val backLeft by lazy { Motor(hw, "backLeft") }
    private val backRight by lazy { Motor(hw, "backRight") }

    val drive by lazy { MecanumDrive(frontLeft, frontRight, backLeft, backRight) }

    val imu by lazy { hw["imu"] as IMU }

    init {
        frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)

        backLeft.motor.direction = DcMotorSimple.Direction.REVERSE

        drive

        imu.initialize(IMU.Parameters(RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.DOWN)))
    }

    fun reset() = imu.resetYaw()

    fun move(x: Double, y: Double, theta: Double) {
        drive.driveFieldCentric(x, y, theta, imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES), true)
    }


    fun stop() = move(0.0, 0.0, 0.0)
}