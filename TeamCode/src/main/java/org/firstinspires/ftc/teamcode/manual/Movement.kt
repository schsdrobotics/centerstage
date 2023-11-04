package org.firstinspires.ftc.teamcode.manual

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.RevIMU
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.hardware.Drivetrain

@TeleOp
class Movement : OpMode() {
    private val frontLeft by lazy { Motor(hardwareMap, "frontLeft") }
    private val frontRight by lazy { Motor(hardwareMap, "frontRight") }
    private val backLeft by lazy { Motor(hardwareMap, "backLeft") }
    private val backRight by lazy { Motor(hardwareMap, "backRight") }

    private val motors by lazy { listOf(frontLeft, frontRight, backLeft, backRight) }

    private val drive by lazy { MecanumDrive(frontLeft, frontRight, backLeft, backRight) }

    private val imu by lazy { RevIMU(hardwareMap) }

    override fun init() {
        frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)

        drive

        imu.init()

    }

    override fun loop() {
        drive.driveFieldCentric(
                -gamepad1.left_stick_x.toDouble(),
                gamepad1.left_stick_y.toDouble(),
                -gamepad1.right_stick_x.toDouble(),
                imu.rotation2d.degrees,
                true,
        )

        motors.forEach { telemetry.addData(it.motor.deviceName, "${(it.motor as DcMotorEx).getCurrent(CurrentUnit.AMPS)} mA") }
    }
}