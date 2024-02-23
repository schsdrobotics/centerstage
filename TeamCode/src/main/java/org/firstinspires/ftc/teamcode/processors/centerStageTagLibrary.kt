package org.firstinspires.ftc.teamcode.processors

import org.firstinspires.ftc.robotcore.external.matrices.VectorF
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary


fun centerStageTagLibrary(): AprilTagLibrary = AprilTagLibrary.Builder()
        .addTag(1, "BlueAllianceLeft",
                2.0, VectorF(61.75f, 41.41f, 4f), DistanceUnit.INCH,
                Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
        .addTag(2, "BlueAllianceCenter",
                2.0, VectorF(61.75f, 35.41f, 4f), DistanceUnit.INCH,
                Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
        .addTag(3, "BlueAllianceRight",
                2.0, VectorF(61.75f, 29.41f, 4f), DistanceUnit.INCH,
                Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
        .addTag(4, "RedAllianceLeft",
                2.0, VectorF(61.75f, -29.41f, 4f), DistanceUnit.INCH,
                Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
        .addTag(5, "RedAllianceCenter",
                2.0, VectorF(61.75f, -35.41f, 4f), DistanceUnit.INCH,
                Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
        .addTag(6, "RedAllianceRight",
                2.0, VectorF(61.75f, -41.41f, 4f), DistanceUnit.INCH,
                Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
        .addTag(7, "RedAudienceWallLarge",
                5.0, VectorF(-70.25f, -40.625f, 5.5f), DistanceUnit.INCH,
                Quaternion(0.5f, -0.5f, -0.5f, 0.5f, 0))
        .addTag(8, "RedAudienceWallSmall",
                2.0, VectorF(-70.25f, -35.125f, 4f), DistanceUnit.INCH,
                Quaternion(0.5f, -0.5f, -0.5f, 0.5f, 0))
        .addTag(9, "BlueAudienceWallSmall",
                2.0, VectorF(-70.25f, 35.125f, 4f), DistanceUnit.INCH,
                Quaternion(0.5f, -0.5f, -0.5f, 0.5f, 0))
        .addTag(10, "BlueAudienceWallLarge",
                5.0, VectorF(-70.25f, 40.625f, 5.5f), DistanceUnit.INCH,
                Quaternion(0.5f, -0.5f, -0.5f, 0.5f, 0))
        .build();