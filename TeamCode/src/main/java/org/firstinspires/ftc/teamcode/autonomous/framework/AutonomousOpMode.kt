package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.arcrobotics.ftclib.command.Command
import com.arcrobotics.ftclib.command.CommandScheduler
import com.arcrobotics.ftclib.command.InstantCommand
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.autonomous.implementations.ClosePreloads
import org.firstinspires.ftc.teamcode.autonomous.implementations.FarCycles
import org.firstinspires.ftc.teamcode.autonomous.implementations.FarPreloads
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions.*
import org.firstinspires.ftc.teamcode.processors.centerStageTagLibrary
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.currentDraw
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import kotlin.math.roundToInt

abstract class AutonomousOpMode(val alliance: Alliance, val side: Side, val nature: Nature) : OpMode() {
    var start = Pose2d(0.0, 0.0, 0.0)

    val builder: TrajectoryActionBuilder by lazy { drive.actionBuilder(start) }
    val drive by lazy { MecanumDrive(hardwareMap, start) }

    val mirrored by lazy {
        when (alliance) {
            Alliance.Red -> recordedPropPosition
            Alliance.Blue -> when (recordedPropPosition) {
                Left -> Right
                Right -> Left
                Middle -> Middle

                else -> Unfound
            }
        }
    }

    val auto by lazy {
        when (nature) {
            Nature.Preloads -> when (side) {
                Side.Backstage -> ClosePreloads(drive, alliance)
                Side.Frontstage -> FarPreloads(drive, alliance)
            }.also { drive.pose = it.start }

            Nature.Cycles -> when (side) {
                Side.Backstage -> throw Exception("oi you can't do that")
                Side.Frontstage -> FarCycles(drive, alliance)
            }.also { drive.pose = it.start }
        }
    }

    val path: AutoActions by lazy {
        when (mirrored) {
            Left -> auto.left
            Middle, Unfound -> auto.middle
            Right -> auto.right
        }
    }

    val propProcessor by lazy { ColourMassDetectionProcessor(alliance.lower, alliance.upper, { MINIMUM_MASS }, { 426.0 }) }

    val tagProcessor by lazy {
        AprilTagProcessor.Builder()
            .setDrawTagID(true)
            .setDrawCubeProjection(true)
            .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
            .setNumThreads(2)
            .setTagFamily(AprilTagProcessor.TagFamily.TAG_standard41h12)
            .setTagLibrary(centerStageTagLibrary())
    }

    val portal by lazy {

        VisionPortal.Builder()
                .setCamera(hardwareMap["front"] as WebcamName)
                .addProcessor(propProcessor)
                .build()
    }

    var recordedPropPosition = Unfound

    override fun init() {
        Robot.initialize(hardwareMap, telemetry, gamepad1, gamepad2, true)
        CommandScheduler.getInstance().reset()

        Robot.PuncherHardware.servo.position = Puncher.State.TWO.position

        drive
    }

    override fun init_loop() {
        drive.pose = auto.start
        drive.updatePoseEstimate()

        telemetry.addData("recorded prop position", propProcessor.recordedPropPosition)
        telemetry.addData("largest detected contour area", "${propProcessor.largestContourArea} pixels")
        telemetry.addData("detected mass center", "(${propProcessor.largestContourX.roundToInt()}, y: ${propProcessor.largestContourY.roundToInt()})")
        telemetry.addData("camera state", portal.cameraState)

        telemetry.addData("drive pose", drive.pose.position.toString())

        CommandScheduler.getInstance().run()
    }

    abstract fun first()


    abstract fun actions(): Command

    override fun start() {
        if (portal.cameraState == VisionPortal.CameraState.STREAMING) {
            portal.stopLiveView()
            portal.stopStreaming()
        }

        recordedPropPosition = getPropPositions()

        first()

        CommandScheduler.getInstance().schedule(actions().andThen(InstantCommand({ requestOpModeStop() })))
    }

    override fun loop() {
        Robot.clearBulkCache()
        Robot.read()

        Robot.DriveHardware.angle = drive.imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES)

        drive.updatePoseEstimate()
        Robot.periodic()
        Robot.write()

        CommandScheduler.getInstance().run()

        telemetry.addData("drive pose", drive.pose.position.toString())
        telemetry.addData("detected", recordedPropPosition)
        telemetry.addData("current", currentDraw)
    }

    override fun stop() {
        CommandScheduler.getInstance().reset()
    }

    private fun getPropPositions(): PropPositions {
        val recorded = propProcessor.recordedPropPosition

        return if (recorded == Unfound) Middle else recorded
    }

    companion object {
        const val MINIMUM_MASS = 7000.0
    }
}