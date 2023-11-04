package org.firstinspires.ftc.teamcode.manual.apriltags

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.matrices.VectorF
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.*
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.*
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion
import org.firstinspires.ftc.teamcode.hardware.Drivetrain
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import kotlin.math.roundToInt

@TeleOp
class AprilTagLocalization : OpMode() {
    private var position = VectorF(0f, 0f, 0f)

    private val drive by lazy { Drivetrain(this) }

    private val processor by lazy {
        AprilTagProcessor.Builder()
            .setDrawAxes(true)
            .setDrawCubeProjection(true)
            .setDrawTagOutline(true)
            .setDrawTagID(true)
            .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
            .setTagLibrary(library)
            .setOutputUnits(INCH, DEGREES)
            .build()
    }

    private val portal by lazy {
        VisionPortal.Builder()
            .setCamera(hardwareMap.get(WebcamName::class.java, "Webcam 1"))
            .addProcessor(processor)
            .build()
    }

    private fun Triple<Double, Double, Double>.round(): Triple<Int, Int, Int> {
        return Triple(this.first.roundToInt(), this.second.roundToInt(), this.third.roundToInt())
    }

    fun processDetections() {
        val detections = processor.detections

        telemetry.addData("# AprilTags Detected", detections.size)

        for (detection in detections) {
            if (detection.metadata != null) {
                telemetry.addLine("==== (ID ${detection.id}) %${detection.metadata.name}")

                val (x, y, z) = Triple(detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z).round()
                val (pitch, roll, yaw) = Triple(detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw).round()
                val (range, bearing, elevation) = Triple(detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation).round()

                telemetry.addLine("Translation Vector: (${x}, ${y}, ${z})")
                telemetry.addLine("Rotational Vector: (${pitch}, ${roll}, ${yaw})")
                telemetry.addLine("(Range, Bearing, Elevation) -> (${range}, ${bearing}, ${elevation})")

                position = VectorF((detection.metadata.fieldPosition.get(0) + detection.ftcPose.x).toFloat(), detection.ftcPose.y.toFloat(), 0f)
            } else {
                telemetry.addLine("\n==== (ID ${detection.id}) Unknown")
                telemetry.addLine("Center ${detection.center.x} ${detection.center.y} (pixels)")
            }
        }

        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.")
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)")
        telemetry.addLine("RBE = Range, Bearing & Elevation")
        telemetry.addData("position", position)
    }

    override fun init() {
        drive
        processor
        portal
    }

    override fun loop() {
        if (gamepad1.dpad_down) {
            portal.stopStreaming()
        } else if (gamepad1.dpad_up) {
            portal.resumeStreaming()
        }

        processDetections()

        drive.loop()
    }

    override fun stop() {
        portal.close()
    }


    companion object {
        private const val sixAndSevenEighths = 6.0 + 7.0 / 8.0

        val library: AprilTagLibrary by lazy {
            AprilTagLibrary.Builder()
                .addTag(0, "LEFT", 0.166, VectorF(0f,0f,6f), METER, Quaternion.identityQuaternion())
                .addTag(1, "CENTER", 0.166, VectorF(46f,0f,7.5f), METER, Quaternion.identityQuaternion())
                .addTag(2, "RIGHT", 0.166, VectorF(93f,0f,7f), METER, Quaternion.identityQuaternion())
            .build()
        }
    }
}