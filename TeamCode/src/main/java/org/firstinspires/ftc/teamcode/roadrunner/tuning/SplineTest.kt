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
        val start = Pose2d(12.0, -63.0, 90.deg)
        if (TuningOpModes.DRIVE_CLASS == MecanumDrive::class.java) {

            val drive = MecanumDrive(hardwareMap, start)
            waitForStart()

            runBlocking(
                    drive.actionBuilder(start)
                            .setTangent(0.deg)
                            .lineToX(7.5) // basic park
                            .setTangent(90.deg)
                            .splineTo(Vector2d(22.0, -42.0), 90.deg) // to backdrop right
                            .splineToSplineHeading(Pose2d(47.0, -43.0, 180.deg), 90.deg) // to backdrop right
                            .setTangent(90.deg)
                            .lineToY(-10.0) // basic park
                            .build()
            )
        } else {
            throw RuntimeException()
        }
    }
}
