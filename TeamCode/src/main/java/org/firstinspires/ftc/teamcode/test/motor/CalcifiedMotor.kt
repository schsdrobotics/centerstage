package org.firstinspires.ftc.teamcode.test.motor

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.frozenmilk.dairy.calcified.Calcified
import dev.frozenmilk.dairy.calcified.hardware.controller.LambdaController
import dev.frozenmilk.dairy.calcified.hardware.motor.Direction
import dev.frozenmilk.dairy.calcified.hardware.motor.ZeroPowerBehaviour
import dev.frozenmilk.dairy.core.FeatureRegistrar
import dev.frozenmilk.dairy.core.OpModeLazyCell

@TeleOp
@Calcified.Attach( // attaches the Calcified feature
        automatedCacheHandling = true, // these are settings for the feature that we can set
        crossPollinate = true, // setting both to true is the default, but if you're a more advanced user you may want to make use of these
)
class CalcifiedMotor : OpMode() {
    init {
        FeatureRegistrar.checkFeatures(this, Calcified)
    }

    private val motor by OpModeLazyCell {
        val motor = Calcified.controlHub.getMotor(0)
        motor.direction = Direction.REVERSE
        motor.cachingTolerance = 0.01
        motor.zeroPowerBehaviour = ZeroPowerBehaviour.BRAKE
        motor
    }


    private val encoder by OpModeLazyCell {
        Calcified.controlHub.getTicksEncoder(0)
    }

    private val controller by OpModeLazyCell {
        LambdaController(0)
                .addMotors(motor)
                .withErrorSupplier(encoder.positionSupplier)
                .appendPController(0.1)
                .appendIController(0.00001, 0.0, 0.2)
                .appendDController(0.005)
    }

    override fun init() {
        motor
        encoder
        controller

        controller.autoUpdate = true
        controller.target = 1000
    }

    override fun loop() {
        controller.update()
    }
}