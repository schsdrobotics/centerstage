package org.firstinspires.ftc.teamcode.autonomous.preloads

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Vector2d
import com.arcrobotics.ftclib.command.CommandScheduler
import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.autonomous.AutonomousPosition
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide.*
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide
import org.firstinspires.ftc.teamcode.autonomous.framework.AutoActions
import org.firstinspires.ftc.teamcode.autonomous.framework.Close
import org.firstinspires.ftc.teamcode.autonomous.framework.Far
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive.DriveAdjustCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.ForwardCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake.Companion.DOWN
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake.Companion.UP
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.IntakeNextCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.IntakeStopCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.IntakeToCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.led.Led
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.TargetGoCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.PuncherDropCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.PuncherOneCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.FlipToCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.Spatula
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
    val start = when (side) {
        Red ->  Pose2d(position.start.position.x, position.start.position.y, position.start.heading.toDouble())
        Blue -> Pose2d(position.start.position.x, -position.start.position.y, position.start.heading.inverse().toDouble())
    }

    val gamepad by lazy { GamepadEx(gamepad1) }

    val drive by lazy { MecanumDrive(hardwareMap, start) }
    val builder by lazy { drive.actionBuilder(start, side) }

    // TODO: implement path mirroring logic for when you flip colors
    val path: AutoActions by lazy {
        val mirrored = when (side) {
            Red -> recordedPropPosition
            Blue -> when (recordedPropPosition) {
                Left -> Right
                Right -> Left
                Middle -> Middle

                else -> Unfound
            }
        }

        when (position) {
            AutonomousPosition.Backstage -> when (mirrored) {
                Left -> Close(drive, side).left
                Middle, Unfound -> Close(drive, side).middle
                Right -> Close(drive, side).right
            }

            AutonomousPosition.Stacks -> when (mirrored) {
                Left -> Far(drive, side).left
                Middle, Unfound -> Far(drive, side).middle
                Right -> Far(drive, side).right
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
        telemetry.addData("drive pose", drive.pose.position.toString())

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

            if (position == AutonomousPosition.Stacks) {
                SequentialCommandGroup(
                    ActionCommand(path.cycle),
                    TargetGoCommand(100, lift),
                    IntakeToCommand(UP - (0.197 / 1.45), intake),
                    ForwardCommand(intake) { 1.0 },
                    WaitCommand(3000),
                    TargetGoCommand(0, lift),
                    IntakeStopCommand(intake),
                    IntakeToCommand(UP, intake)
                )
            } else InstantCommand({ }),

            ActionCommand(path.white),
            TargetGoCommand(175, lift),
            FlipToCommand(Spatula.State.AUTO, spatula),
            TargetGoCommand(125, lift),

            DriveAdjustCommand(Vector2d(-0.175, 0.0), 0.0, { currentDraw >= 2.5 }, drive),
            WaitCommand(250),
            PuncherOneCommand(puncher),
            WaitCommand(250),

            ActionCommand(path.yellow),
            TargetGoCommand(175, lift),
            FlipToCommand(Spatula.State.AUTO, spatula),
            TargetGoCommand(125, lift),

            DriveAdjustCommand(Vector2d(-0.175, 0.0), 0.0, { currentDraw >= 3.0 }, drive),
            WaitCommand(250),
            PuncherDropCommand(puncher),
            WaitCommand(250),

            ParallelCommandGroup(
                ActionCommand(path.park),
                SequentialCommandGroup(
                    TargetGoCommand(200, lift),
                    FlipToCommand(Spatula.State.TRANSFER, spatula),
                    TargetGoCommand(0, lift)
                )
            ),
        ))
    }

    override fun loop() {
        telemetry.addData("drive pose", drive.pose.position.toString())
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