package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.hardware.rev.RevColorSensorV3
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.math.abs
import kotlin.math.round

enum class Color(val r: Double, val g: Double, val b: Double) {
    WHITE(0.6, 1.0, 0.8),
    GREEN(0.3, 1.0, 0.4),
    PURPLE(0.5, 0.7, 1.0),
    YELLOW(0.7, 1.0, 0.3),

    UNKNOWN(NEGATIVE_INFINITY, NEGATIVE_INFINITY, NEGATIVE_INFINITY)
}

var color = Color.UNKNOWN
@TeleOp
class ColorSensingDemo : OpMode() {
    private val sensor by lazy { hardwareMap.colorSensor["sensor"] as RevColorSensorV3 }

    private fun subtract(a: List<Double>, b: Color): Triple<Double, Double, Double> {
        return Triple(a[0] - b.r, a[1] - b.g, a[2] - b.b)
    }

    override fun init() { }

    override fun loop() {
        val colors = Triple(sensor.red(), sensor.green(), sensor.blue())
        val components = colors.toList().map { it.toDouble() }
        val relative = components
                .map { it / components.max() }
                .map { round(it * 10.0) / 10.0 }

        val (red, green, blue) = relative


        val differences = Color.values().map { Pair(it, subtract(relative, it)) }

        telemetry.addData("differences", differences)



        val least = differences.minBy { abs(it.second.first + it.second.second + it.second.third) }


        telemetry.addData("colors", colors)

        telemetry.addData("red", red)
        telemetry.addData("green", green)
        telemetry.addData("blue", blue)

        telemetry.addData("least", least)

        telemetry.addData("optical", sensor.rawOptical())
        telemetry.addData("red1", sensor.normalizedColors.red)
        telemetry.addData("green2", sensor.normalizedColors.green)
        telemetry.addData("blue1", sensor.normalizedColors.blue)
        telemetry.addData("blue1", sensor.normalizedColors.alpha)
    }
}