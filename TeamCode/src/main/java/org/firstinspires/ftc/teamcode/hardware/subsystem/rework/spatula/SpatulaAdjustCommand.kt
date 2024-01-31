package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula

import com.arcrobotics.ftclib.command.InstantCommand

class SpatulaAdjustCommand(amount: Double, spatula: Spatula) : InstantCommand({ spatula.adjust(amount) })