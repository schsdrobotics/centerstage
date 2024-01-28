package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive

import com.arcrobotics.ftclib.command.SubsystemBase
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot.LogoFacingDirection
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot.UsbFacingDirection
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.Spatula
import org.firstinspires.ftc.teamcode.util.SlewRateLimiter
import org.firstinspires.ftc.teamcode.util.structures.Vector3
import org.firstinspires.ftc.teamcode.util.structures.plus
import kotlin.math.pow

class Drive(val hw: HardwareMap, val telemetry: Telemetry, val gamepad: GamepadEx, val auto: Boolean = false) : SubsystemBase() {

    private val frontLeft by lazy { Motor(hw, "frontLeft") }
    private val frontRight by lazy { Motor(hw, "frontRight") }
    private val backLeft by lazy { Motor(hw, "backLeft") }
    private val backRight by lazy { Motor(hw, "backRight") }

    val drive by lazy { MecanumDrive(frontLeft, frontRight, backLeft, backRight) }

    val limiters = listOf(
        SlewRateLimiter(10.0, -10.0),
        SlewRateLimiter(10.0, -10.0)
    )

    var last = 0.0

    val angle
        get() = imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES)

    val fed
        get() = Vector3(-gamepad.leftX, -gamepad.leftY, -gamepad.rightX)

    val imu by lazy { (hw["imu"] as IMU) }

    init {
        frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)

        backLeft.motor.direction = DcMotorSimple.Direction.REVERSE

        drive

        imu.initialize(IMU.Parameters(RevHubOrientationOnRobot(LogoFacingDirection.LEFT, UsbFacingDirection.DOWN)))
    }

    fun reset() { imu.resetYaw() }

    fun move(x: Double, y: Double, theta: Double) {
        drive.driveFieldCentric(x, y, theta, angle, true)
    }

    override fun periodic() {
        val slewed = Vector3(limiters[0].calculate(fed.x), limiters[1].calculate(fed.y), fed.z)

        val corrective = Vector3(0.0, 0.0, 0.0)

        val final = slewed + corrective

        if (!auto) move(final.x, final.y, final.z)

        telemetry.addData("imu angle", angle)
    }

    fun stop() = move(0.0, 0.0, 0.0)

    companion object {
        object Gains {
            const val HEADING = 0.01
        }
    }
}