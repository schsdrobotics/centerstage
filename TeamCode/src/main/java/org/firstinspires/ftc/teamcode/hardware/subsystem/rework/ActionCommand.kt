package org.firstinspires.ftc.teamcode.hardware.subsystem.rework

import com.acmerobotics.dashboard.FtcDashboard

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.arcrobotics.ftclib.command.Command
import com.arcrobotics.ftclib.command.Subsystem


class ActionCommand(action: Action, requirements: Set<Subsystem> = emptySet()) : Command {
    private val action: Action
    private val requirements: Set<Subsystem>
    var finished = false

    init {
        this.action = action
        this.requirements = requirements
    }

    override fun isFinished() = finished

    override fun getRequirements(): Set<Subsystem> {
        return requirements
    }

    override fun execute() {
        val packet = TelemetryPacket()
        action.preview(packet.fieldOverlay())
        finished = !action.run(packet)
        FtcDashboard.getInstance().sendTelemetryPacket(packet)
    }
}