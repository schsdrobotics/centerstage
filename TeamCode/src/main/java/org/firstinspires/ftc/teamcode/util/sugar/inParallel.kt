package org.firstinspires.ftc.teamcode.util.sugar

import org.mercurialftc.mercurialftc.scheduler.commands.Command
import org.mercurialftc.mercurialftc.scheduler.commands.ParallelCommandGroup

fun inParallel(vararg commands: Command) = ParallelCommandGroup().addCommands(commands.toList())