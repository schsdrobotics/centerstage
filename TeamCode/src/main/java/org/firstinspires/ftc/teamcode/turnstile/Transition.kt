package org.firstinspires.ftc.teamcode.turnstile

data class Transition<S>(val from: S, val to: S, val action: () -> Unit, val condition: () -> Boolean) {
    class ChainBuilder<S> {
        val transitions = mutableListOf<Transition<S>>()

        fun transition(from: S, to: S, rest: Masher.() -> Unit) {
            val things = Masher().apply(rest)

            if (things.action == null) throw Exception("Did not assign action to transition.")
            if (things.condition == null) throw Exception("Did not assign condition to transition.")

            transitions.add(Transition(from, to, things.action!!, things.condition!!))
        }
    }

    class Masher {
        var action: (() -> Unit)? = null
        var condition: (() -> Boolean)? = null

        fun at(condition: () -> Boolean) {
            this.condition = condition
        }

        fun perform(action: () -> Unit) {
            this.action = action
        }
    }
}

data class TransitionPair<S>(val from: S, val to: S)