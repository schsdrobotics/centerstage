package org.firstinspires.ftc.teamcode.roadrunner

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Vector2d
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.autonomous.framework.Auto.Companion.MAJOR_APOTHEM
import org.firstinspires.ftc.teamcode.autonomous.framework.Auto.Companion.MINOR_APOTHEM
import org.firstinspires.ftc.teamcode.processors.centerStageTagLibrary
import org.firstinspires.ftc.teamcode.roadrunner.FusionDrive.Parameters.coefficients
import org.firstinspires.ftc.teamcode.roadrunner.FusionDrive.Parameters.offset
import org.firstinspires.ftc.teamcode.roadrunner.messages.PoseMessage
import org.firstinspires.ftc.teamcode.util.control.KalmanFilter
import org.firstinspires.ftc.teamcode.util.extensions.VectorFExtensions.toVector2d
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import kotlin.math.cos
import kotlin.math.sin

class FusionDrive(hw: HardwareMap, pose: Pose2d, val processor: AprilTagProcessor) : MecanumDrive(hw, pose) {
    val filter = KalmanFilter.Vector2dKalmanFilter(coefficients.Q, coefficients.R)
    var filtered = Vector2d(0.0, 0.0)

    override fun updatePoseEstimate(): PoseVelocity2d {
        val twist = localizer.update()

        val predicted = pose + twist.value()

        val obtained = positionFromTags()

        pose = if (obtained == Vector2d(0.0, 0.0)) {
            filtered = filter.update(twist.value(), predicted.position)

            predicted
        } else {
            filtered = filter.update(twist.value(), obtained)

            Pose2d(filtered, predicted.heading)
        }

        poseHistory.add(pose)

        while (poseHistory.size > 100) {
            poseHistory.removeFirst();
        }

        estimatedPoseWriter.write(PoseMessage(pose))

        return twist.velocity().value()
    }

    fun positionFromTags(): Vector2d {
        val detections = processor.detections.filterNotNull()

        return detections
            .map { toGlobalReference(it, pose.heading.toDouble()) }
            .fold(Vector2d(0.0, 0.0)) { a, b -> a + b }
            .div(detections.size.toDouble())
    }

    fun toGlobalReference(detection: AprilTagDetection, heading: Double): Vector2d {
        val detected = detection.metadata.fieldPosition.toVector2d()

        val robot = detected - offset

        val inverted = -heading

        val rotated = Vector2d(
            robot.x * cos(inverted) + robot.y * sin(inverted),
            robot.x * -sin(inverted) + robot.y * cos(inverted),
        )

        val pose = centerStageTagLibrary()
            .lookupTag(detection.id)
            .fieldPosition
            .toVector2d()

        return if (detection.id <= 6) {
            Vector2d(pose.x + rotated.y, pose.y - rotated.x)
        } else {
            Vector2d(pose.x - rotated.y, pose.y + rotated.x)
        }
    }

    object Parameters {
        val offset = Vector2d(MINOR_APOTHEM, MAJOR_APOTHEM)

        val coefficients = KalmanFilter.KalmanCoefficients(0.1, 0.4)
    }
}