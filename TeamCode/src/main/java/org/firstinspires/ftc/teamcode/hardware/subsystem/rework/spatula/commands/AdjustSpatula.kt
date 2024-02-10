package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.commands

import com.arcrobotics.ftclib.command.InstantCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.Spatula

class AdjustSpatula(amount: Double, spatula: Spatula) : InstantCommand({ spatula.adjust(amount) })