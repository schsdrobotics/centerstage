package org.firstinspires.ftc.teamcode.library

import com.qualcomm.robotcore.hardware.Gamepad

class Macro(vararg val buttons: GamepadEx.Button, action: () -> Unit)

class GamepadEx(private val gamepad: Gamepad) {
    private val listeners: List<Macro> = listOf()

    fun inner() = gamepad

    fun pressed(button: Button) = readInstanceProperty<Boolean>(gamepad, button.name)
    fun analog(button: Button) = readInstanceProperty<Float>(gamepad, button.name)

    val left_stick
        get() = Point(gamepad.left_stick_x, gamepad.left_stick_y)

    val right_stick
        get() = Point(gamepad.right_stick_x, gamepad.right_stick_y)

    data class Point(val x: Float, val y: Float)

    sealed class Button(val name: String) {
        object X : Button("x")
        object Y : Button("y")
        object A : Button("a")
        object B : Button("b")

        object DPad {
            object Up : Button("dpad_up")
            object Down : Button("dpad_down")
            object Left : Button("dpad_left")
            object Right : Button("dpad_right")
        }

        object Trigger {
            object Left : Button("left_trigger")
            object Right : Button("right_trigger")
        }

        object Bumper {
            object Left : Button("left_bumper")
            object Right : Button("right_bumper")
        }

        object Joystick {
            object Left : Button("left_stick")
            object Right : Button("right_stick")
        }

        object Misc {
            object Start : Button("start")
            object Guide : Button("guide")
            object Back : Button("back")
        }
    }
}