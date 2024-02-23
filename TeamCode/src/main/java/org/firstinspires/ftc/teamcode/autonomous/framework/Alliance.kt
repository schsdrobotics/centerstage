package org.firstinspires.ftc.teamcode.autonomous.framework

import org.firstinspires.ftc.teamcode.util.sugar.hsv
import org.opencv.core.Scalar

enum class Alliance(val lower: Scalar, val upper: Scalar) {
    Red(hsv(0.0, 20.0, 20.0), hsv(30.0, 100.0, 100.0)),
    Blue(hsv(195.0, 30.0, 30.0), hsv(260.0, 100.0, 100.0))
}