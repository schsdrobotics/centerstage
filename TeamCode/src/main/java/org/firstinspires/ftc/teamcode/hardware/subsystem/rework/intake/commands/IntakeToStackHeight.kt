package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands

import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake
import org.firstinspires.ftc.teamcode.hardware.Robot.IntakeHardware.Configuration.PIXEL_THICKNESS


class IntakeToStackHeight(height: Double, intake: Intake) : IntakeTo(intake.angleForHeight(height * PIXEL_THICKNESS), intake)