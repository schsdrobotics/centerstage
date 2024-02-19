package org.firstinspires.ftc.teamcode.util.structures

import kotlin.math.PI

object Angle {
	private const val TAU = PI * 2

	/**
	 * Returns [angle] clamped to `[0, 2pi]`.
	 *
	 * @param angle angle measure in radians
	 */
	@JvmStatic
	fun norm(angle: Double): Double {
		var modifiedAngle = angle % TAU

		modifiedAngle = (modifiedAngle + TAU) % TAU

		return modifiedAngle
	}

	/**
	 * Returns [angleDelta] clamped to `[-pi, pi]`.
	 *
	 * @param angleDelta angle delta in radians
	 */
	@JvmStatic
	fun normDelta(angleDelta: Double): Double {
		var modifiedAngleDelta = norm(angleDelta)

		if (modifiedAngleDelta > PI) {
			modifiedAngleDelta -= TAU
		}

		return modifiedAngleDelta
	}
}