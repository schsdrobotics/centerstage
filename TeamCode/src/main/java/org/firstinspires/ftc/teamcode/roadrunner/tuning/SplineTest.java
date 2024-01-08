package org.firstinspires.ftc.teamcode.roadrunner.tuning;

import static java.util.Collections.singletonList;

import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.TankDrive;

import java.util.Arrays;
import java.util.Collections;


public final class SplineTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(12.0, -62.0, Math.toRadians(90));
        if (TuningOpModes.DRIVE_CLASS.equals(MecanumDrive.class)) {
            MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

            waitForStart();

            /*
.lineTo(new Vector2d(21.08, -62.45))
.lineToLinearHeading(new Pose2d(6.36, -37.39, Math.toRadians(130.00)))
            * */

            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
//                            .splineTo(new Vector2d(10.00, -39.00), Math.toRadians(155.00)) // left
//                            .lineToY(drive.pose.position.y + 28) // middle
//                            .splineTo(new Vector2d(16.00, -39.00), Math.toRadians(45.00)) // right

                            .build());
        } else {
            throw new RuntimeException();
        }
    }
}
