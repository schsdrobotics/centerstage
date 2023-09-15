package org.firstinspires.ftc.teamcode.turnstile

class StateMachine<S>(initial: S, private val transitions: List<Transition<S>>) {
    private val listeners: MutableList<() -> Unit> = mutableListOf()
    private var state = initial

    fun all() = transitions
    fun pseudo() = transitions.filter { it.from == state }
    fun valid() = pseudo().filter { it.condition() }

    fun at(state: S) = this.state == state
    fun can(next: S) = transitions.any { (it.from == state) && (it.to == next) }
    fun cannot(next: S) = !can(next)

    fun onTransition(perform: () -> Unit) {
        listeners.add(perform)
    }

    fun transition(to: S): Result {
        return when {
            cannot(to) -> return Err("cannot transition to state $to")
            can(to) -> {
                val transition = transitions.first { it.from == state && it.to == to }
                transition.action()
                state = to
                listeners.forEach { it() }
                Ok("successfully executed transition from ${transition.from} to $to")
            }

            else -> { Err("reached impossible state") }
        }
    }

    fun transition(transition: Transition<S>): Result {
        return transition(transition.to)
    }

    fun tick(): Result {
        val valid = valid()

        return when {
            valid.isEmpty() -> Ok("no valid states to transition to")
            valid.size == 1 -> transition(valid.first())
            else -> Err("multiple valid transitions from state $state")
        }
    }

    companion object {
        fun <S> of(initial: S, block: Transition.ChainBuilder<S>.() -> Unit): StateMachine<S> {
            return StateMachine(initial, Transition.ChainBuilder<S>().apply(block).transitions)
        }

        sealed class Result
        class Ok<T>(val data: T) : Result()
        class Err<E>(val data: E) : Result()
    }
}