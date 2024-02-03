package org.firstinspires.ftc.teamcode.autonomous.purplePixel

import com.acmerobotics.roadrunner.Vector2d
import com.arcrobotics.ftclib.command.CommandScheduler
import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.ParallelDeadlineGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.Subsystem
import com.arcrobotics.ftclib.command.WaitCommand
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.autonomous.AutonomousPosition
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide.Blue
import org.firstinspires.ftc.teamcode.autonomous.framework.AutoActions
import org.firstinspires.ftc.teamcode.autonomous.framework.Close
import org.firstinspires.ftc.teamcode.autonomous.framework.Far
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive.DriveAdjustCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.led.Led
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.TargetGoCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.PuncherDropCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.PuncherOneCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.Spatula
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.FlipToCommand
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions.Left
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions.Middle
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions.Right
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions.Unfound
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.currentDraw
import org.firstinspires.ftc.vision.VisionPortal


// TODO: refactor this whole system to individualize actions like purple pixel and place on backdrop to their own thingso
abstract class Preloads(val side: AutonomousSide, val position: AutonomousPosition) : OpMode() {
    val puncher by lazy { Puncher(hardwareMap, telemetry, spatula, Puncher.State.TWO) }
    val spatula by lazy { Spatula(hardwareMap, telemetry, lift) }
    val lift by lazy { Lift(hardwareMap, telemetry) }
    val intake by lazy { Intake(hardwareMap) }
    val led by lazy { Led(hardwareMap) }

    // TODO: refactor when mirror logic exists to remove this redundancy
    val start = position.start

    val gamepad by lazy { GamepadEx(gamepad1) }

    val builder by lazy { drive.actionBuilder(start, side == Blue) }
    val drive by lazy { MecanumDrive(hardwareMap, start) }

    // TODO: implement path mirroring logic for when you flip colors
    val path: AutoActions by lazy {
        when (position) {
            AutonomousPosition.Close -> when (recordedPropPosition) {
                Left -> Close(drive, builder).left
                Middle, Unfound -> Close(drive, builder).middle
                Right -> Close(drive, builder).right
            }

            AutonomousPosition.Far -> when (recordedPropPosition) {
                Left -> Far(drive, builder).left
                Middle, Unfound -> Far(drive, builder).middle
                Right -> Far(drive, builder).right
            }
        }
    }

    val processor by lazy { ColourMassDetectionProcessor(side.lower, side.upper, { MINIMUM_MASS }, { 426.0 }) }

    val portal by lazy {
        VisionPortal.Builder()
            .setCamera(hardwareMap["back"] as WebcamName)
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
        telemetry.addData("recorded prop position", processor.recordedPropPosition)
        telemetry.addData("largest detected contour area", processor.largestContourArea)
        telemetry.addData("detected mass center", "x: " + processor.largestContourX + ", y: " + processor.largestContourY)
        telemetry.addData("camera state", portal.cameraState)

        CommandScheduler.getInstance().run()
    }

    override fun start() {
        if (portal.cameraState == VisionPortal.CameraState.STREAMING) {
            portal.stopLiveView()
            portal.stopStreaming()
        }

        recordedPropPosition = getPropPositions()

        CommandScheduler.getInstance().schedule(SequentialCommandGroup(
            ActionCommand(path.purple),

            ParallelCommandGroup(
                ActionCommand(path.yellow),
                TargetGoCommand(150, lift),
                FlipToCommand(Spatula.State.TRANSFER, spatula),
            ),

            FlipToCommand(Spatula.State.SCORE, spatula),
            TargetGoCommand(125, lift),

            DriveAdjustCommand(Vector2d(-0.25, 0.0), 0.0, { currentDraw >= 2.5 }, drive),
            WaitCommand(250),
            PuncherDropCommand(puncher),

            DriveAdjustCommand(Vector2d(0.15, 0.0), 0.0, { false }, drive),
            WaitCommand(500),

            FlipToCommand(Spatula.State.TRANSFER, spatula),
            TargetGoCommand(0, lift),
            DriveAdjustCommand(Vector2d(0.0, 0.0), 0.0, { true }, drive),
        ))
    }

    override fun loop() {
        telemetry.addData("detected", recordedPropPosition)

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