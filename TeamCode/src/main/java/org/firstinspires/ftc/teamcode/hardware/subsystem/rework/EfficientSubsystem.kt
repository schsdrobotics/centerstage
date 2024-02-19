package org.firstinspires.ftc.teamcode.hardware.subsystem.rework

import com.arcrobotics.ftclib.command.SubsystemBase

abstract class EfficientSubsystem : SubsystemBase() {
	abstract override fun periodic()
	abstract fun read()
	abstract fun write()
	abstract fun reset()
}