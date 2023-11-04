package org.firstinspires.ftc.teamcode.test

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.hardware.Lift
import org.firstinspires.ftc.teamcode.hardware.Lift.Position.*

@TeleOp
class LiftTesting : OpMode() {
    val left by lazy { hardwareMap.dcMotor["left"] as DcMotorEx }
    val right by lazy { hardwareMap.dcMotor["right"] as DcMotorEx }
    val imu by lazy { hardwareMap["imu"] as IMU }

    val leftLift by lazy { Lift(left, imu, reverse=true) }
    val rightLift by lazy { Lift(right, imu) }

    override fun init() {
        right.direction = DcMotorSimple.Direction.REVERSE
        left.direction = DcMotorSimple.Direction.REVERSE
    }

    override fun loop() {
        val target = when {
            gamepad1.square -> LOW
            gamepad1.triangle -> MID
            gamepad1.circle -> HIGH
            gamepad1.x -> ZERO
            else -> null
        }

        if (target != null) {
            leftLift.target(target)
            rightLift.target(target)
        }

        when {
            gamepad1.right_bumper -> {
                leftLift.go()
                rightLift.go()
            }

            gamepad1.left_bumper -> {
                leftLift.stop()
                rightLift.stop()
            }

            gamepad1.guide -> {
                leftLift.zero()
                rightLift.zero()
            }
        }

        leftLift.loop()
        rightLift.loop()

        telemetry.addData("left target pos", left.targetPosition)
        telemetry.addData("left current pos", left.currentPosition)
        telemetry.addData("left target", leftLift.position)
        telemetry.addData("left current", left.getCurrent(CurrentUnit.AMPS))

        telemetry.addData("right target pos", right.targetPosition)
        telemetry.addData("right current pos", right.currentPosition)
        telemetry.addData("right target", rightLift.position)
        telemetry.addData("right current", right.getCurrent(CurrentUnit.AMPS))
    }
}