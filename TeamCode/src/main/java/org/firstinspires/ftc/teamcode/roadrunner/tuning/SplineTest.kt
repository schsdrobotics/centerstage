package org.firstinspires.ftc.teamcode.roadrunner.tuning

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.autonomous.purplePixel.PurplePixel
import org.firstinspires.ftc.teamcode.autonomous.purplePixel.PurplePixel.Companion.WIDTH
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg

class SplineTest : LinearOpMode() {
    @Throws(InterruptedException::class)

    override fun runOpMode() {
        val start = Pose2d(12.0, -62.0, Math.toRadians(90.0))
        if (TuningOpModes.DRIVE_CLASS == MecanumDrive::class.java) {
            val drive = MecanumDrive(hardwareMap, start)
            waitForStart()

            runBlocking(drive.actionBuilder(start)
                    .splineTo(Vector2d(12.0, -(25.0 + PurplePixel.HEIGHT / 2.0)), 90.deg) // purple pixel
                    .setReversed(true)
                    .setTangent(0.deg)
                    .lineToXLinearHeading(50.0, 180.deg) // to backdrop middle
                    .setTangent(90.deg)
                    .lineToY(-10.0) // basic park
                    .build())
        } else {
            throw RuntimeException()
        }
    }
}
