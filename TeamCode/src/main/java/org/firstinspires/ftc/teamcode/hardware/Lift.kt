package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot.xyzOrientation
import com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION
import com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER
import com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.mercurialftc.mercurialftc.scheduler.commands.LambdaCommand
import kotlin.math.abs

class Lift(val motor: DcMotorEx, val imu: IMU, val tipThreshold: Double = 30.0, val reverse: Boolean = false) {
    var position = Position.ZERO

    init {
        zero()
        motor.targetPosition = position.ticks
        motor.direction = REVERSE
        motor.zeroPowerBehavior = BRAKE

        val rotation = xyzOrientation(0.0, 0.0, 0.0)
        val orientation = RevHubOrientationOnRobot(rotation)

        imu.initialize(IMU.Parameters(orientation))
    }

    val pitch: Double
        get() = imu.robotYawPitchRollAngles.getPitch(AngleUnit.DEGREES)

    val roll: Double
        get() = imu.robotYawPitchRollAngles.getRoll(AngleUnit.DEGREES)


    fun target(position: Position) {
        this.position = position
    }

    fun zero() {
        motor.mode = STOP_AND_RESET_ENCODER
    }

    fun stop() {
        motor.power = 0.0
    }

    fun go(power: Double = 1.0) {
        motor.mode = RUN_WITHOUT_ENCODER
        motor.mode = RUN_TO_POSITION
        motor.power = power
    }

    fun loop() {
        if (abs(pitch) > tipThreshold) {
            target(Position.ABSOLUTE_ZERO)
        }

        motor.targetPosition = position.ticks * if (reverse) -1 else 1
    }

    enum class Position(val ticks: Int) {
        ABSOLUTE_ZERO(((50.0 / 1600.0) * 610.0).toInt()),
        ZERO(((100.0 / 1600.0) * 610.0).toInt()),
        LOW(((450.0 / 1600.0) * 610.0).toInt()),
        MID(((820.0 / 1600.0) * 610.0).toInt()),
        HIGH(((1600.0 / 1600.0) * 610.0).toInt())
    }
}