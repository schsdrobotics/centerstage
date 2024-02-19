package org.firstinspires.ftc.teamcode.hardware.devices

import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.HardwareDevice
import org.firstinspires.ftc.teamcode.util.structures.Angle
import kotlin.math.abs

class AbsoluteAnalogEncoder(val encoder: AnalogInput, private val analogRange: Double = DEFAULT_RANGE) : HardwareDevice {
	private var wraparound = false
	private var offset = 0.0
	var direction = false
		private set
	fun zero(off: Double): AbsoluteAnalogEncoder {
		offset = off
		return this
	}

	fun setInverted(invert: Boolean): AbsoluteAnalogEncoder {
		direction = invert
		return this
	}

	fun setWraparound(wraparound: Boolean): AbsoluteAnalogEncoder {
		this.wraparound = wraparound
		return this
	}

	private var pastPosition = 1.0

	val position: Double
		get() {
			var pos: Double = Angle.norm((if (!direction) 1 - voltage / analogRange else voltage / analogRange) * Math.PI * 2 - offset)

			if (wraparound && pos > Math.PI * 1.5) {
				pos -= Math.PI * 2
			}

			//checks for crazy values when the encoder is close to zero
			if (!VALUE_REJECTION || abs(Angle.normDelta(pastPosition)) > 0.1 || abs(Angle.normDelta(pos)) < 1) pastPosition = pos
			return pastPosition
		}
	val voltage: Double
		get() = encoder.voltage

	override fun getManufacturer() = null

	override fun getDeviceName() = "AbsoluteAnalogEncoder"

	override fun getConnectionInfo() = null

	override fun getVersion() = 0

	override fun resetDeviceConfigurationForOpMode() {}
	override fun close() {}

	companion object {
		var DEFAULT_RANGE = 3.3
		var VALUE_REJECTION = false
	}
}