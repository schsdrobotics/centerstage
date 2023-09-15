package org.firstinspires.ftc.teamcode.manual

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.turnstile.StateMachine

sealed class State
object Bottom : State()
object Low : State()
object Medium : State()
object High : State()

enum class Targets(val ticks: Int) {
    BOTTOM(0),
    LOW(-1501),
    MEDIUM(-2212),
    HIGH(-3060)
}

@TeleOp
class theSecond: OpMode() {
    private lateinit var lift: DcMotor
    private lateinit var pawl: Servo
    private var target = Targets.BOTTOM
    private var atTarget = false
    private lateinit var model: StateMachine<State>

    override fun init() {
        lift = hardwareMap.dcMotor["lift"]

        pawl = hardwareMap.servo["pawl"]

        lift.power = 0.0

        pawl.scaleRange(0.0, 0.12)

        lift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        model = StateMachine.of(Bottom) {
            transition(Bottom, High) {
                at { gamepad1.a && target == Targets.HIGH }
                perform {
                    lift.targetPosition = Targets.HIGH.ticks
                }
            }

            transition(Bottom, Medium) {
                at {gamepad1.a && target == Targets.MEDIUM }
                perform {
                    lift.targetPosition = Targets.MEDIUM.ticks
                }
            }

            transition(Bottom, Low) {
                at { gamepad1.a && target == Targets.LOW }
                perform {
                    lift.targetPosition = Targets.LOW.ticks
                }
            }
        }

        model.onTransition { goToPosition() }
    }

    override fun loop() {
//        telemetry.addData("Pos", lift.currentPosition)
//
//        when {
//            gamepad1.x -> target = Targets.HIGH
//            gamepad1.y -> target = Targets.MEDIUM
//            gamepad1.b -> target = Targets.LOW
//        }
//
//        if (lift.currentPosition <= target.ticks) {
//            lift.power = 0.0
//            atTarget = true
//        }
//
//        if (atTarget && gamepad1.a) {
//            lift.targetPosition = Targets.BOTTOM.ticks
//            unlock()
//            goToPosition()
//            lock()
//            atTarget = false
//        }
//
//        model.tick()
        lock()
    }

    fun goToPosition() {
        lift.power = 0.1
        lift.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    fun lock() {
        pawl.position = 0.0
    }

    fun unlock() {
        pawl.position = 1.0
    }
}