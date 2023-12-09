package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import kotlin.math.max

class Feburary(val hardwareMap: HardwareMap, val opmode: OpMode) {
    val processor by lazy { AprilTagProcessor.easyCreateWithDefaults() }

    val portal by lazy {
        VisionPortal.easyCreateWithDefaults(hardwareMap["Webcam 1"] as WebcamName, processor)
    }



    val detections
        get() = processor.detections

    val detected: Option<AprilTagDetection>
        get() {
            return if (detections.isEmpty()) {
                None()
            } else {
                Some(detections.first())
            }
        }

    fun init() { processor; portal }

    fun test() {
        val currentDetections = processor.detections.toList()
        opmode.telemetry.addData("# AprilTags Detected", currentDetections.size)
    }

    fun feed(): Vector3 {
        return when (detected) {
            is None -> Vector3.Zero
            is Some -> {
                val tag = (detected as Some<AprilTagDetection>).value

                if (tag.id != DESIRED.value) return Vector3.Zero

                val (x, y, z) = Triple(
                    tag.ftcPose.x,
                    max(tag.ftcPose.y - DESIRED_PROXIMITY, DESIRED_PROXIMITY),
                    tag.ftcPose.z
                )

                opmode.telemetry.addData("x", x * Gains.X)
                opmode.telemetry.addData("y", y * Gains.Y)
                opmode.telemetry.addData("z", z * Gains.Z)

                return Vector3(x * Gains.X, y * Gains.Y, z * Gains.Z)
            }
        }
    }

    companion object {
        private const val DESIRED_PROXIMITY = 5.0

        private object Gains {
            const val X = 0.1
            const val Y = 0.1
            const val Z = 0.1
        }

        enum class Position {
            Left,
            Middle,
            Right
        }

        private val DESIRED = Some(5)

        private val DESIRE_MAP = mapOf(
            Position.Left   to listOf(1, 4),
            Position.Middle to listOf(2, 5),
            Position.Right  to listOf(3, 6),
        )
    }
}