package org.firstinspires.ftc.teamcode.roadrunner.tuning

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.Close
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

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
