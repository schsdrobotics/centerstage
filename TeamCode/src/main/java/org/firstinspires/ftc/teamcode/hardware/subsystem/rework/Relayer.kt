package org.firstinspires.ftc.teamcode.hardware.subsystem.rework

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.lynx.commands.standard.LynxSetModuleLEDColorCommand
import org.firstinspires.ftc.teamcode.hardware.Robot.Hubs.CONTROL
import org.firstinspires.ftc.teamcode.hardware.Robot.Hubs.EXPANSION

class Relayer : EfficientSubsystem() {
	var selected = SelectedHub.Control

	override fun periodic() { }

	override fun read() { }

	override fun write() { }

	override fun reset() { }

	fun selectedToIndicator(indicator: Indicator) {
		val (r, g, b) = indicator.asBytes()
		val it = selected.module

		it.sendCommand(LynxSetModuleLEDColorCommand(it, r, g, b))
	}

	enum class Indicator(val r: Int, val g: Int, val b: Int) {
		Yellow(255, 255, 0),
		Green(0, 255, 0),
		Purple(200, 0, 255),
		White(255, 0, 255);

		fun asBytes() = Triple(r.toByte(), g.toByte(), b.toByte())
	}

	enum class SelectedHub(val module: LynxModule) {
		Control(CONTROL),
		Expansion(EXPANSION),
	}
}