package org.firstinspires.ftc.teamcode.library

import com.qualcomm.robotcore.util.ElapsedTime
import java.util.concurrent.TimeUnit

class LoopTimer {
    private val elapsed = ElapsedTime()
    val times = mutableListOf<Long>(0)

    fun reset() = elapsed.reset()

    fun record() {
        times.add(elapsed.time(TimeUnit.MILLISECONDS))
        return reset()
    }

    fun average() = times.sum() / times.size
}