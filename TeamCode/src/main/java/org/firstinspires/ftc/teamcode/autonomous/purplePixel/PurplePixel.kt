package org.firstinspires.ftc.teamcode.autonomous.purplePixel

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.arcrobotics.ftclib.command.CommandScheduler
import com.arcrobotics.ftclib.command.Subsystem
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide.Blue
import org.firstinspires.ftc.teamcode.autonomous.framework.Close
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake
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


// TODO: refactor this whole system to individualize actions like purple pixel and place on backdrop to their own things
@Autonomous(group = "Purple Pixel")
abstract class PurplePixel(val side: AutonomousSide, val close: Boolean = true) : OpMode() {
    val puncher by lazy { Puncher(hardwareMap, telemetry) }
    val spatula by lazy { Spatula(hardwareMap, telemetry, lift) }
    val lift by lazy { Lift(hardwareMap, telemetry) }
    val intake by lazy { Intake(hardwareMap) }

    // TODO: refactor when mirror logic exists to remove this redundancy
    val start = Close.start

    val builder by lazy { drive.actionBuilder(start, side == Blue) }
    val drive by lazy { MecanumDrive(hardwareMap, start) }

    // TODO: implement path mirroring logic for when you flip colors
    val path by lazy {
        when (recordedPropPosition) {
            Left -> Close(builder).left
            Middle -> Close(builder).middle
            Right -> Close(builder).right

            Unfound -> builder.build()
        }
    }

    val processor by lazy { ColourMassDetectionProcessor(side.lower, side.upper, { MINIMUM_MASS }, { 213.0 }, { 426.0 }) }

    val portal by lazy {
        VisionPortal.Builder()
            .setCamera(hardwareMap["back"] as WebcamName)
            .addProcessor(processor)
            .build()
    }

    var recordedPropPosition = Unfound

    open fun register(vararg subsystems: Subsystem) {
        CommandScheduler.getInstance().registerSubsystem(*subsystems)
    }

    override fun init() {
        CommandScheduler.getInstance().reset()

        drive

        CommandScheduler.getInstance().schedule(

        )
    }

    override fun init_loop() {
        telemetry.addData("Big Contour woah very big", processor.largestContourArea)
        telemetry.addData("Currently Recorded Position", processor.recordedPropPosition)
        telemetry.addData("Camera State", portal.cameraState)
        telemetry.addData("Currently Detected Mass Center", "x: " + processor.largestContourX + ", y: " + processor.largestContourY)
        telemetry.addData("Currently Detected Mass Area", processor.largestContourArea)
    }

    override fun start() {
        if (portal.cameraState == VisionPortal.CameraState.STREAMING) {
            portal.stopLiveView()
            portal.stopStreaming()
        }

        recordedPropPosition = getPropPositions()
    }

    var b = true

    override fun loop() {
        telemetry.addData("detected", recordedPropPosition)
        if (b) b = path.run(TelemetryPacket())
        CommandScheduler.getInstance().run()
    }

    override fun stop() { CommandScheduler.getInstance().reset() }

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