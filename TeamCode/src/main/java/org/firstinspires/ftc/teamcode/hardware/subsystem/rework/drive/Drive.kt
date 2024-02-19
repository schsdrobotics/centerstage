package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive

import com.arcrobotics.ftclib.gamepad.GamepadEx
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.hardware.Robot.DriveHardware.angle
import org.firstinspires.ftc.teamcode.hardware.Robot.DriveHardware.drive
import org.firstinspires.ftc.teamcode.hardware.Robot.DriveHardware.imu
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.EfficientSubsystem
import org.firstinspires.ftc.teamcode.util.structures.Vector3
import kotlin.math.pow
import kotlin.math.sign

class Drive(val telemetry: Telemetry, val gamepad: GamepadEx) : EfficientSubsystem() {
    var input = Vector3.Zero

    fun move(x: Double, y: Double, theta: Double) { drive.driveFieldCentric(x, y, theta, angle, true) }

    override fun reset() { imu.resetYaw() }

    override fun periodic() {
        telemetry.addData("imu angle", angle)
    }

    fun updateHeading() {
        angle = imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES)
    }

    override fun read() {
        input = Vector3(-gamepad.leftX, -gamepad.leftY, -gamepad.rightX.let { it.pow(2) * it.sign })
    }

    override fun write() { move(input.x, input.y, input.z) }
}