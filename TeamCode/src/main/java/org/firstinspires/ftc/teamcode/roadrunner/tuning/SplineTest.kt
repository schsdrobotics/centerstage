package org.firstinspires.ftc.teamcode.roadrunner.tuning

import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.Close
import org.firstinspires.ftc.teamcode.autonomous.purplePixel.PurplePixel
import org.firstinspires.ftc.teamcode.autonomous.purplePixel.PurplePixel.Companion.WIDTH
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg

class SplineTest : LinearOpMode() {
    @Throws(InterruptedException::class)

    override fun runOpMode() {
        if (TuningOpModes.DRIVE_CLASS == MecanumDrive::class.java) {

            val drive = MecanumDrive(hardwareMap, Close.start)
            val builder = drive.actionBuilder(Close.start)

            waitForStart()
        } else {
            throw RuntimeException()
        }
    }
}
