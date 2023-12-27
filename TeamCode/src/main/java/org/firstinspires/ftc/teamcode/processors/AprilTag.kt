package org.firstinspires.ftc.teamcode.processors

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor

/**
 * position relative to center in ***INCHES***
 *
 * x = backdrop (+) to other side of field (-)
 *
 * y = across truss (blue truss = neg side, red truss = pos side)
 */
data class FieldPosition(val x: Float, val y: Float)

// TODO change this
@TeleOp
class AprilTag : OpMode() {

    private val UNKNOWN_POS = FieldPosition(0f, 0f)
    var position: FieldPosition = UNKNOWN_POS
        private set

    private val processor: AprilTagProcessor by lazy {
        AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build()
    }
    private val visionPortal: VisionPortal by lazy {
        VisionPortal.Builder()
                .addProcessor(processor)
                .enableLiveView(true)
                .setCamera(hardwareMap.get("Webcam 1") as WebcamName)
                .build()
    }

    override fun init() {
        visionPortal.resumeStreaming()
    }

    override fun loop() {
        if (processor.detections.isNotEmpty()) {
            val startId = processor.detections[0].id
            val rawPos = processor.detections[0].metadata.fieldPosition
            val posOnField = FieldPosition(rawPos.data[0], -rawPos.data[1])
            while (processor.detections.isNotEmpty() && processor.detections[0].id == startId) {
                val d = processor.detections[0]
                val right = d.ftcPose.x.toFloat()
                val out = d.ftcPose.y.toFloat()

                this.position = FieldPosition(posOnField.x - out, posOnField.y - right)
            }
        }
    }
}