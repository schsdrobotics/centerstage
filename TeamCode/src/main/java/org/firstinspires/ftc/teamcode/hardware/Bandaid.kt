package org.firstinspires.ftc.teamcode.hardware

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles

class Bandaid(private val imu: IMU) : IMU by imu {
    private var offset = 0.0

    override fun resetYaw() {
        offset = imu.robotYawPitchRollAngles.getYaw(DEGREES)
    }

    override fun getRobotYawPitchRollAngles(): YawPitchRollAngles {
            val ypr = imu.robotYawPitchRollAngles
            val yaw = ypr.getYaw(DEGREES) - offset

            return YawPitchRollAngles(DEGREES,
                    ((yaw + 180) % 360) - 180,
                    ypr.getPitch(DEGREES),
                    ypr.getRoll(DEGREES),
                    ypr.acquisitionTime
            )
        }
}