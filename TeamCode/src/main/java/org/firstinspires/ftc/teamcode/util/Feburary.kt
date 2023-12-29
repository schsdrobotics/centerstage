package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.util.extensions.*
import org.firstinspires.ftc.teamcode.util.structures.None
import org.firstinspires.ftc.teamcode.util.structures.Option
import org.firstinspires.ftc.teamcode.util.structures.Some
import org.firstinspires.ftc.teamcode.util.structures.Vector3
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import kotlin.math.cos
import kotlin.math.sin

class Feburary(val hardwareMap: HardwareMap, val opmode: OpMode) {
    val processor by lazy { AprilTagProcessor.Builder().setOutputUnits(DistanceUnit.INCH, AngleUnit.RADIANS).build() }

    val portal by lazy {
        VisionPortal.easyCreateWithDefaults(hardwareMap["front"] as WebcamName, processor)
    }

    val detections
        get() = processor.detections

    val detected: Option<AprilTagDetection>
        get() = if (detections.isEmpty()) {
            None()
        } else {
            Some(detections.first())
        }

    val averagePosition: Option<AprilTagPoseFtc>
        get() = if (detections.isNotEmpty()) {
            Some(detections.map { it.ftcPose }.reduce { a, b -> a + b } / detections.size)
        } else {
            None()
        }

    fun init() { processor; portal }

    fun test() {
        val currentDetections = processor.detections.toList()
        opmode.telemetry.addData("detected size", currentDetections.size)
        opmode.telemetry.addData("detected", currentDetections.map { it.id }.joinToString(", "))
    }

    fun feed(mode: Mode = Average): Vector3 {
        return when (detected) {
            is None -> Vector3.Zero
            is Some -> {
                val tag = (detected as Some).value

                if (mode is Target && tag.id != mode.target) Vector3.Zero

                val position = if (mode is Target) tag.ftcPose else (averagePosition as Some).value

                val (bearing, range, yaw) = Triple(position.bearing, position.range, position.yaw)
                val (x, y, z) = Triple(sin(bearing) * range, cos(bearing) * range, -yaw)

                opmode.telemetry.addData("x", (x - CAMERA_OFFSET) * Gains.X)
                opmode.telemetry.addData("y", (y - DESIRED_PROXIMITY) * Gains.Y)
                opmode.telemetry.addData("z", z * Gains.Z)

                opmode.telemetry.addData("pose x", position.x)
                opmode.telemetry.addData("pose y", position.y)
                opmode.telemetry.addData("pose z", position.z)

                Vector3(x * Gains.X, y * Gains.Y, z * Gains.Z)
            }
        }
    }

    companion object {
        private const val DESIRED_PROXIMITY = 3.0
        private const val CAMERA_OFFSET = 6.0

        private object Gains {
            const val X = -0.05
            const val Y = -0.05
            const val Z = -0.05
        }

        enum class Position {
            Left,
            Middle,
            Right
        }

        sealed class Mode
        object Average : Mode()
        class Target(val target: Int) : Mode()


        private val DESIRE_MAP = mapOf(
            Position.Left   to listOf(1, 4),
            Position.Middle to listOf(2, 5),
            Position.Right  to listOf(3, 6),
        )
    }
}