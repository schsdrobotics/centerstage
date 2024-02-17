package org.firstinspires.ftc.teamcode.test.subsystem

import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit

@TeleOp(group = "!")
class DepositTest : CommandOpMode() {
    val gamepad by lazy { GamepadEx(gamepad1) }
    val deposit by lazy { Deposit(hardwareMap, telemetry) }

    override fun initialize() {
        register(deposit)
    }


    override fun run() {
        super.run()

        deposit.state = Deposit.State(-gamepad.rightY * Deposit.SCORE_ANGLE, gamepad.rightX * Deposit.HORIZONTAL_BOUND)

        telemetry.addData("state", deposit.state)
        telemetry.addData("vertical", Deposit.Kinematics.vertical(deposit.state.vertical))
        telemetry.addData("horizontal", Deposit.Kinematics.horizontal(deposit.state.horizontal))
        telemetry.update()
    }
}