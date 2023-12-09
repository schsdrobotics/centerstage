package org.firstinspires.ftc.teamcode.util

import org.opencv.core.Scalar

fun hsv(h: Double, s: Double, v: Double) = Scalar(h / 2.0, s / 100.0 * 255.0, v / 100.0 * 255.0)