package org.firstinspires.ftc.teamcode.turnstile

sealed class State
object Solid : State()
object Liquid : State()
object Gas : State()

fun main() {
    var temperature = 0

    val model = StateMachine.of<State>(Solid) {
        transition(Liquid, Solid) {
            at { temperature == 0 }
            perform { println("yo it's mad cold, ice go crazy") }
        }

        transition(Solid, Liquid) {
            at { temperature > 0 }
            perform { println("my ice be melting ):") }
        }

        transition(Liquid, Gas) {
            at { temperature == 100 }
            perform { println("science technology engineering art math!!") }
        }

        transition(Gas, Liquid) {
            at { temperature < 100 }
            perform { println("c o n d e n s e") }
        }
    }

    println("temperature: $temperature")

    for (i in 1..11) {
        temperature += 10
        println("temperature: $temperature")
        model.tick()
    }

    for (i in 1..11) {
        temperature -= 10
        println("temperature: $temperature")
        model.tick()
    }
}