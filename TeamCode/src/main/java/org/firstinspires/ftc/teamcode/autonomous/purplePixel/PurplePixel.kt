package org.firstinspires.ftc.teamcode.autonomous.purplePixel

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.ftc.*
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide.*
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions.*
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg
import org.firstinspires.ftc.vision.VisionPortal
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import kotlin.math.PI


// TODO: refactor this whole system to individualize actions like purple pixel and place on backdrop to their own things
@Autonomous(group = "Purple Pixel")
open class PurplePixel(val side: AutonomousSide) : OpModeEX() {
    // TODO: refactor when mirror logic exists to remove this redundancy
    val start = when (side) {
        Red -> Pose2d(12.0, -62.0, 90.deg)
        Blue -> Pose2d(12.0, 62.0, 90.deg)
    }


    val drive by lazy { MecanumDrive(hardwareMap, start) }

    // TODO: implement path mirroring logic for when you flip colors
    val path by lazy {
        when (recordedPropPosition) {
            Left -> drive.actionBuilder(start)
                    .splineTo(Vector2d(6.5, -35.5), 135.deg) // purple pixel
                    .setReversed(true)
                    .splineTo(Vector2d(50.0, -35.0), 0.deg) // to backdrop left
                    .setTangent(90.deg)
                    .lineToY(-10.0) // basic park
                    .build()

            Middle -> drive.actionBuilder(start)
                .splineTo(Vector2d(12.0, -(25.0 + HEIGHT / 2.0)), 90.deg) // purple pixel
                .setReversed(true)
                .setTangent(0.deg)
                .lineToXLinearHeading(50.0, 180.deg) // to backdrop middle
                .setTangent(90.deg)
                .lineToY(-10.0) // basic park
                .build()

            Right -> drive.actionBuilder(start)
                    .splineToSplineHeading(Pose2d(50.0, -35.0, 180.deg), 0.deg) // to backdrop right
                    .setTangent(180.deg)
                    .splineTo(Vector2d(25.0 + (WIDTH / 2.0), -30.0), 180.deg) // purple pixel right
                    .setTangent(45.deg)
                    .lineToY(-10.0) // basic park
                    .build()

            Unfound -> drive.actionBuilder(start).build()
        }
    }

    val processor by lazy { ColourMassDetectionProcessor(side.lower, side.upper, { MINIMUM_MASS }, { 213.0 }, { 426.0 }) }

    val portal by lazy {
        VisionPortal.Builder()
            .setCamera(hardwareMap["front"] as WebcamName)
            .addProcessor(processor)
            .build()
    }

    var recordedPropPosition = Unfound

    override fun registerBindings() = Unit


    override fun registerSubsystems() {}

    override fun initEX() { drive }

    override fun init_loopEX() {
        telemetry.addData("Big Contour woah very big", processor.largestContourArea)
        telemetry.addData("Currently Recorded Position", processor.recordedPropPosition)
        telemetry.addData("Camera State", portal.cameraState)
        telemetry.addData("Currently Detected Mass Center", "x: " + processor.largestContourX + ", y: " + processor.largestContourY)
        telemetry.addData("Currently Detected Mass Area", processor.largestContourArea)
    }

    override fun startEX() {
        if (portal.cameraState == VisionPortal.CameraState.STREAMING) {
            portal.stopLiveView()
            portal.stopStreaming()
        }

        recordedPropPosition = getPropPositions()

        runBlocking(path)
    }

    override fun loopEX() { telemetry.addData("detected", recordedPropPosition) }

    override fun stopEX() = Unit

    private fun getPropPositions(): PropPositions {
        val recorded = processor.recordedPropPosition

        return if (recorded == Unfound) Left else recorded
    }

    companion object {
        const val MINIMUM_MASS = 8200.0

        const val HEIGHT = 16.0
        const val WIDTH = 11.0
    }
}