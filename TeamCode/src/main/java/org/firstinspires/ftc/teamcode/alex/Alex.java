package org.firstinspires.ftc.teamcode.alex;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class Alex extends OpMode {
    private DcMotor motor;

    @Override
    public void init() {
        motor = hardwareMap.dcMotor.get("lift");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        motor.setPower(0.0);

        if(gamepad1.dpad_up) {
            motor.setPower(1.0);
        }

        if(gamepad1.dpad_down) {
            motor.setPower(-1.0);
        }

    }
}
