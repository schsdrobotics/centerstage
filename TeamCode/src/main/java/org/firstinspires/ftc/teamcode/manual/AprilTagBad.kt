package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import kotlin.math.roundToInt


/**
 * This 2023-2024 OpMode illustrates the basics of AprilTag recognition and pose estimation,
 * including Java Builder structures for specifying Vision parameters.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 */
@TeleOp(name = "AprilTagForRealz", group = "!")
class AprilTagBad : LinearOpMode() {
    /**
     * [.aprilTag] is the variable to store our instance of the AprilTag processor.
     */
    private var aprilTag: AprilTagProcessor? = null

    /**
     * [.visionPortal] is the variable to store our instance of the vision portal.
     */
    private var visionPortal: VisionPortal? = null
    override fun runOpMode() {
        initAprilTag()

        // Wait for the DS start button to be touched.
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream")
        telemetry.addData(">", "Touch Play to start OpMode")
        telemetry.update()
        waitForStart()
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                telemetryAprilTag()

                // Push telemetry to the Driver Station.
                telemetry.update()

                // Save CPU resources; can resume streaming when needed.
                if (gamepad1.dpad_down) {
                    visionPortal!!.stopStreaming()
                } else if (gamepad1.dpad_up) {
                    visionPortal!!.resumeStreaming()
                }

                // Share the CPU.
                sleep(20)
            }
        }

        // Save more CPU resources when camera is no longer needed.
        visionPortal!!.close()
    } // end method runOpMode()

    /**
     * Initialize the AprilTag processor.
     */
    private fun initAprilTag() {

        // Create the AprilTag processor.
        aprilTag = AprilTagProcessor.Builder()
            .setDrawAxes(true)
            .setDrawCubeProjection(true)
            .setDrawTagOutline(true)
            .setDrawTagID(true)
            .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
            .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
            .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES) // == CAMERA CALIBRATION ==
            // If you do not manually specify calibration parameters, the SDK will attempt
            // to load a predefined calibration for your camera.
            //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
            // ... these parameters are fx, fy, cx, cy.
            .build()

        // Create the vision portal by using a builder.
        val builder = VisionPortal.Builder()

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName::class.java, "Webcam 1"))
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK)
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableCameraMonitoring(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag)

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build()

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);
    }

    private fun Triple<Double, Double, Double>.round(): Triple<Int, Int, Int> {
        return Triple(this.first.roundToInt(), this.second.roundToInt(), this.third.roundToInt())
    }

    private fun telemetryAprilTag() {
        val currentDetections: List<AprilTagDetection> = aprilTag!!.detections
        telemetry.addData("# AprilTags Detected", currentDetections.size)

        // Step through the list of detections and display info for each one.
        for (detection in currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine("==== (ID ${detection.id}) %${detection.metadata.name}")

                val (x, y, z) = Triple(detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z).round()
                val (pitch, roll, yaw) = Triple(detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw).round()
                val (range, bearing, elevation) = Triple(detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation).round()

                telemetry.addLine("Translation Vector: (${x}, ${y}, ${z})")
                telemetry.addLine("Rotational Vector: (${pitch}, ${roll}, ${yaw})")
                telemetry.addLine("(Range, Bearing, Elevation) -> (${range}, ${bearing}, ${elevation})")
            } else {
                telemetry.addLine("\n==== (ID ${detection.id}) Unknown")
                telemetry.addLine("Center ${detection.center.x} ${detection.center.y} (pixels)")
            }
        }

        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.")
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)")
        telemetry.addLine("RBE = Range, Bearing & Elevation")
    }

    companion object {
        private const val USE_WEBCAM = true
    }
}

