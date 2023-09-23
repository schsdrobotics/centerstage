package org.firstinspires.ftc.teamcode.manual

import TeamPropDetector
import android.util.Size
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal

@TeleOp
class TeamPropDetection : OpMode() {
    private val detector = TeamPropDetector()

    private val visionPortal by lazy {
        // Create the vision portal by using a builder.
        VisionPortal.Builder()
            .setCamera(hardwareMap.get("Webcam 1") as WebcamName)
            .setCameraResolution(Size(640, 480))
            .enableLiveView(true)
            .setStreamFormat(VisionPortal.StreamFormat.YUY2)
            .addProcessor(detector)
    }


    override fun init() {
        visionPortal
    }

    override fun loop() {
        telemetry.addData("amoutn", detector.contours.size)
    }
}