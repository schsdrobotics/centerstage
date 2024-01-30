package org.firstinspires.ftc.teamcode.autonomous.purplePixel

import com.acmerobotics.roadrunner.NullAction
import com.acmerobotics.roadrunner.Vector2d
import com.arcrobotics.ftclib.command.CommandScheduler
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.Subsystem
import com.arcrobotics.ftclib.command.WaitCommand
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide.Blue
import org.firstinspires.ftc.teamcode.autonomous.framework.AutoActions
import org.firstinspires.ftc.teamcode.autonomous.framework.Close
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive.DriveAdjustCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake
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
import org.firstinspires.ftc.vision.VisionPortal


// TODO: refactor this whole system to individualize actions like purple pixel and place on backdrop to their own thingso
abstract class Preloads(val side: AutonomousSide, val close: Boolean = true) : OpMode() {
    val puncher by lazy { Puncher(hardwareMap, telemetry, spatula, Puncher.State.TWO) }
    val spatula by lazy { Spatula(hardwareMap, telemetry, lift) }
    val lift by lazy { Lift(hardwareMap, telemetry) }
    val intake by lazy { Intake(hardwareMap) }

    // TODO: refactor when mirror logic exists to remove this redundancy
    val start = Close.start

    val gamepad by lazy { GamepadEx(gamepad1) }

    val builder by lazy { drive.actionBuilder(start, side == Blue) }
    val drive by lazy { MecanumDrive(hardwareMap, start) }

    // TODO: implement path mirroring logic for when you flip colors
    val path: AutoActions by lazy {
        when (recordedPropPosition) {
            Left -> Close(drive, builder).left
            Middle -> Close(drive, builder).middle
            Right -> Close(drive, builder).right

            Unfound -> AutoActions(NullAction(), NullAction())
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

        spatula.spatula.position = Spatula.State.SCORE.position
    }

    override fun init_loop() {
        telemetry.addData("largest detected contour area", processor.largestContourArea)
        telemetry.addData("recorded prop position", processor.recordedPropPosition)
        telemetry.addData("camera state", portal.cameraState)
        telemetry.addData("detected mass center", "x: " + processor.largestContourX + ", y: " + processor.largestContourY)

        spatula.angle

        CommandScheduler.getInstance().run()
    }

    override fun start() {
        if (portal.cameraState == VisionPortal.CameraState.STREAMING) {
            portal.stopLiveView()
            portal.stopStreaming()
        }

        recordedPropPosition = Right

        CommandScheduler.getInstance().schedule(SequentialCommandGroup(
            TargetGoCommand(200, lift)
                .andThen(FlipToCommand(Spatula.State.AUTO, spatula))
                .andThen(TargetGoCommand(100, lift)),
            ActionCommand(path.purple),
            PuncherOneCommand(puncher),
            TargetGoCommand(150, lift),
            FlipToCommand(Spatula.State.TRANSFER, spatula),
            ActionCommand(path.yellow),
            FlipToCommand(Spatula.State.SCORE, spatula),
            WaitCommand(500),
            TargetGoCommand(125, lift),
            DriveAdjustCommand(Vector2d(-0.15, 0.0), 0.0, { puncher.distance == 0.0 || puncher.distance <= 14.0 }, drive),
//            DriveAdjustCommand(Vector2d(-0.2, 0.0), 0.0, { puncher.distance == 0.0 || puncher.distance >= 7.0 }, drive),
            WaitCommand(250),
            PuncherDropCommand(puncher),
            WaitCommand(500),
            FlipToCommand(Spatula.State.TRANSFER, spatula),
            TargetGoCommand(0, lift),
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