package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Pose2d
import com.arcrobotics.ftclib.command.Command
import com.arcrobotics.ftclib.command.CommandScheduler
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.autonomous.implementations.Close
import org.firstinspires.ftc.teamcode.autonomous.implementations.Far
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.led.Led
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.Spatula
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions.Left
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions.Middle
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions.Right
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions.Unfound
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.vision.VisionPortal

abstract class AutonomousOpMode(val side: AutonomousSide, val position: AutonomousPosition) : OpMode() {
    val puncher by lazy { Puncher(hardwareMap, telemetry, spatula, Puncher.State.TWO) }
    val spatula by lazy { Spatula(hardwareMap, telemetry, lift) }
    val lift by lazy { Lift(hardwareMap, telemetry) }
    val intake by lazy { Intake(hardwareMap) }
    val led by lazy { Led(hardwareMap) }

    var start = Pose2d(0.0, 0.0, 0.0)

    val gamepad by lazy { GamepadEx(gamepad1) }

    val drive by lazy { MecanumDrive(hardwareMap, start) }
    val builder by lazy { drive.actionBuilder(start) }

    val mirrored by lazy {
        when (side) {
            AutonomousSide.Red -> recordedPropPosition
            AutonomousSide.Blue -> when (recordedPropPosition) {
                Left -> Right
                Right -> Left
                Middle -> Middle

                else -> Unfound
            }
        }
    }

    val auto by lazy {
        when (position) {
            AutonomousPosition.Backstage -> Close(drive, side)
            AutonomousPosition.Stacks -> Far(drive, side)
        }.also { drive.pose = it.start }
    }

    val path: AutoActions by lazy {
        when (mirrored) {
            Left -> auto.left
            Middle, Unfound -> auto.middle
            Right -> auto.right
        }
    }

    val processor by lazy { ColourMassDetectionProcessor(side.lower, side.upper, { MINIMUM_MASS }, { 426.0 }) }

    val portal by lazy {
        VisionPortal.Builder()
                .setCamera(hardwareMap["front"] as WebcamName)
                .addProcessor(processor)
                .build()
    }

    var recordedPropPosition = Unfound

    override fun init() {
        CommandScheduler.getInstance().reset()

        CommandScheduler.getInstance().registerSubsystem(led)

        drive
    }

    override fun init_loop() {
        drive.pose = auto.start

        telemetry.addData("recorded prop position", processor.recordedPropPosition)
        telemetry.addData("largest detected contour area", processor.largestContourArea)
        telemetry.addData("detected mass center", "x: " + processor.largestContourX + ", y: " + processor.largestContourY)
        telemetry.addData("camera state", portal.cameraState)
        drive.updatePoseEstimate()
        telemetry.addData("drive pose", drive.pose.position.toString())


        CommandScheduler.getInstance().run()
    }

    abstract fun first(): Unit


    abstract fun actions(): Command

    override fun start() {
        if (portal.cameraState == VisionPortal.CameraState.STREAMING) {
            portal.stopLiveView()
            portal.stopStreaming()
        }

        recordedPropPosition = getPropPositions()

        first()

        CommandScheduler.getInstance().schedule(actions())
    }

    override fun loop() {
        telemetry.addData("drive pose", drive.pose.position.toString())
        telemetry.addData("detected", recordedPropPosition)

        CommandScheduler.getInstance().run()
    }

    override fun stop() {
        CommandScheduler.getInstance().reset()
    }

    private fun getPropPositions(): PropPositions {
        val recorded = processor.recordedPropPosition

        return if (recorded == Unfound) Middle else recorded
    }

    companion object {
        const val MINIMUM_MASS = 7000.0

        const val HEIGHT = 16.0
        const val WIDTH = 11.0
    }
}