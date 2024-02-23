package org.firstinspires.ftc.teamcode.util.extensions

import com.acmerobotics.roadrunner.Vector2d
import org.firstinspires.ftc.robotcore.external.matrices.VectorF

object VectorFExtensions {
    fun VectorF.toVector2d() = Vector2d(this[0].toDouble(), this[1].toDouble())
}