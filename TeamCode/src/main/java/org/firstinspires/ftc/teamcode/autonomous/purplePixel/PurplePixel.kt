package org.firstinspires.ftc.teamcode.autonomous.purplePixel

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide
import org.firstinspires.ftc.teamcode.hardware.subsystem.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.Wrist
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions
import org.firstinspires.ftc.teamcode.processors.ColourMassDetectionProcessor.PropPositions.*
import org.firstinspires.ftc.vision.VisionPortal
import org.mercurialftc.mercurialftc.scheduler.OpModeEX
import kotlin.math.roundToInt

open class PurplePixel(val side: AutonomousSide) : OpModeEX() {
    private val frontLeft by lazy { Motor(hardwareMap, "frontLeft") }
    private val frontRight by lazy { Motor(hardwareMap, "frontRight") }
    private val backLeft by lazy { Motor(hardwareMap, "backLeft") }
    private val backRight by lazy { Motor(hardwareMap, "backRight") }

    val drive by lazy { MecanumDrive(frontLeft, frontRight, backLeft, backRight) }

    val imu by lazy { hardwareMap["imu"] as IMU }

    val timer = ElapsedTime()

    val processor by lazy {
        ColourMassDetectionProcessor(side.lower, side.upper, { minimum }, { 213.0 }, { 426.0 })
    }

    val portal by lazy {
        VisionPortal.Builder()
            .setCamera(hardwareMap["front"] as WebcamName)
            .addProcessor(processor)
            .build()
    }

    var recordedPropPosition = Unfound

    override fun registerBindings() = Unit


    override fun registerSubsystems() {}

    override fun initEX() {
        frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)

        drive

        imu.initialize(
                IMU.Parameters(
                        RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                                RevHubOrientationOnRobot.UsbFacingDirection.UP
                        )
                )
        )

    }

    override fun init_loopEX() {
        telemetry.addData("Big Contour woah very big", processor.largestContourArea)
        telemetry.addData("Currently Recorded Position", processor.recordedPropPosition)
        telemetry.addData("Camera State", portal.cameraState)
        telemetry.addData("Currently Detected Mass Center", "x: " + processor.largestContourX + ", y: " + processor.largestContourY)
        telemetry.addData("Currently Detected Mass Area", processor.largestContourArea)
    }

    override fun startEX() {
        telemetry.addData("started", true)

        if (portal.cameraState == VisionPortal.CameraState.STREAMING) {
            portal.stopLiveView()
            portal.stopStreaming()
        }

        recordedPropPosition = getPropPositions()

        telemetry.addData("got", true)

        if (side == AutonomousSide.Red) {
            when (recordedPropPosition) {
                Left -> {
                    spells.add(Drive(0.0, -0.5, 0.0, 1850)) // forward
                    spells.add(Drive(0.0, 0.0, 0.6, 750)) // spin left
                    spells.add(Drive(0.0, -0.4, 0.0, 250)) // forward
                    spells.add(Drive(0.0, 0.5, 0.0, 500)) // backward
                }

                Middle -> {
                    spells.add(Drive(0.0, -0.5, 0.0, 2500)) // forward
                    spells.add(Drive(0.0, 0.4, 0.0, 2000)) // backward
                    spells.add(Drive(0.0, 0.0, 0.6, 800)) // spin left
                }

                Right -> {
                    spells.add(Drive(-0.65, 0.0, 0.0, 750)) // right
                    spells.add(Drive(0.0, -0.5, 0.0, 1500)) // forward
                    spells.add(Drive(0.0, 0.5, 0.0, 1300)) // backward
                    spells.add(Drive(0.0, 0.0, 0.6, 750)) // spin left
                }

                Unfound -> {}
            }

            spells.add(Drive(0.0, 0.5, 0.0, 1800)) // backward

            if (recordedPropPosition == Right) {
                spells.add(Drive(0.5, 0.6, 0.0, 500)) // left
            }
        } else {
            when (recordedPropPosition) {
                Left -> {
                    spells.add(Drive(0.0, -0.55, 0.0, 1600)) // forward
                    spells.add(Drive(0.0, 0.0, 0.6, 750)) // spin left
                    spells.add(Drive(0.0, 0.5, 0.0, 850)) // backward
                }

                Middle -> {
                    spells.add(Drive(0.0, -0.5, 0.0, 2500)) // forward
                    spells.add(Drive(0.0, 0.4, 0.0, 2000)) // backward
                    spells.add(Drive(0.0, 0.0, -0.6, 800)) // spin left
                    spells.add(Drive(0.0, 0.5, 0.0, 2800)) // backward
                }

                Right -> {
                    spells.add(Drive(0.0, -0.5, 0.0, 2300)) // forward
                    spells.add(Drive(0.0, 0.0, -0.6, 750)) // spin right
                    spells.add(Drive(0.0, -0.5, 0.0, 900)) // forward
                    spells.add(Drive(0.0, 0.5, 0.0, 10000)) // backward
                }

                Unfound -> {}
            }
        }



        timer.reset()
        telemetry.addData("reset", true)
    }

    val spells = mutableListOf<Incantation>()

    var index = 0

    override fun loopEX() {
        telemetry.addData("timer count", timer.milliseconds().roundToInt())
        telemetry.addData("detected", recordedPropPosition)
        telemetry.addData("spells", spells.size)
        telemetry.addData("index", index)

        val spell = spells.getOrNull(index) ?: return

        telemetry.addData("spell", spell)

        when (spell) {
            is Drive -> {
                if (timer.milliseconds() <= spell.time) {
                    drive.driveRobotCentric(spell.x, spell.y, spell.z, true)
                } else {
                    drive.driveRobotCentric(0.0, 0.0, 0.0, true)
                    index++
                    timer.reset()
                }
            }

            is Wait -> {
                if (timer.milliseconds() <= spell.time) {
                    return
                } else {
                    index++
                    timer.reset()
                }
            }

            is Score -> {
                Lift(this).to(Lift.Position.MID).execute()
                Wrist(this, Lift(this)).deposit().execute()
            }
        }
    }

    override fun stopEX() = Unit

    private fun getPropPositions(): PropPositions {
        val recorded = processor.recordedPropPosition

        return if (recorded == Unfound) Left else recorded
    }

    companion object {
        const val minimum = 8200.0
    }

    sealed class Incantation
    data class Drive(val x: Double, val y: Double, val z: Double, val time: Int) : Incantation()
    data class Wait(val time: Int) : Incantation()
    object Score : Incantation()
}