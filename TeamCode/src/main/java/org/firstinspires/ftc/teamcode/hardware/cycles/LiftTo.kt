package org.firstinspires.ftc.teamcode.hardware.cycles

import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.TargetGoCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.FlipToCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.Spatula

class LiftTo(position: Lift.Position, lift: Lift, spatula: Spatula) : InstantCommand({ to(position, lift, spatula).schedule() }) {
    companion object {
        fun to(target: Lift.Position, lift: Lift, spatula: Spatula) =
                when {
                    // default case: at zero, target non-zero
                    // align, go, score
                    lift.atZero && target != Lift.Position.ZERO -> {
//                    condition = ("lift.atZero && target != ZERO")
//                    logged = target.toString()
                        SequentialCommandGroup(
                                FlipToCommand(Spatula.State.ALIGN, spatula),
                                TargetGoCommand(target, lift),
                                FlipToCommand(Spatula.State.SCORE, spatula),
                        )
                    }

                    // adjustment case: lift not zeroed, target isn't zero
                    // go + score
                    lift.position >= Lift.Position.CLEAR.ticks && target != Lift.Position.ZERO -> {
//                    condition = ("lift.position >= CLEAR.ticks && target != ZERO")
//                    logged = target.toString()
                        SequentialCommandGroup(
                                TargetGoCommand(target, lift),
                                FlipToCommand(Spatula.State.SCORE, spatula),
                        )
                    }

                    //
                    lift.position <= 30 && target == Lift.Position.ZERO -> {
//                    condition = ("lift.position <= 30 && target == ZERO")
//                    logged = target.toString()

                        SequentialCommandGroup(
                                FlipToCommand(Spatula.State.ALIGN, spatula),
                                TargetGoCommand(Lift.Position.ZERO, lift),
                                FlipToCommand(Spatula.State.TRANSFER, spatula),
                        )
                    }

                    // restorative case: target is zero
                    // ensure lift is high enough before dropping
                    // align, wait 300ms, (go + transfer)
                    target == Lift.Position.ZERO -> {
//                    condition = ("target == ZERO")
//                    logged = target.toString()

                        SequentialCommandGroup(
                                if (!lift.cleared) TargetGoCommand(300, lift) else InstantCommand(),
                                FlipToCommand(Spatula.State.ALIGN, spatula),
                                ParallelCommandGroup(
                                        TargetGoCommand(Lift.Position.ZERO, lift),
                                        FlipToCommand(Spatula.State.TRANSFER, spatula)
                                )
                        )
                    }

                    // base case
                    else -> {
//                    condition = "base case"
//                    logged = target.toString()

                        SequentialCommandGroup(
                                TargetGoCommand(target, lift),
                                FlipToCommand(Spatula.State.SCORE, spatula),
                        )
                    }
                }
    }
}