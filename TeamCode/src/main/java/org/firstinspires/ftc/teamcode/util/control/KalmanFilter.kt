package org.firstinspires.ftc.teamcode.util.control

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Twist2d
import com.acmerobotics.roadrunner.Vector2d


class KalmanFilter {
    /**
     * @param Q model covariance (trust in model), default 0.1
     * @param R sensor covariance (trust in sensor), default 0.4
     */
    class KalmanCoefficients(val Q: Double, val R: Double)

    var x = 0.0 // your initial state
    var p = 1.0 // your initial covariance guess
    var K = 1.0 // your initial Kalman gain guess
    val kalman: KalmanCoefficients

    /**
     * @param Q model covariance (trust in model), default 0.1
     * @param R sensor covariance (trust in sensor), default 0.4
     */
    constructor(Q: Double, R: Double) {
        kalman = KalmanCoefficients(Q, R)
    }

    constructor(kalman: KalmanCoefficients) {
        this.kalman = kalman
    }

    var x_previous = x
    var p_previous = p
    var u = 0.0
    var z = 0.0

    /** Run in your loop.
     *
     * @param model the CHANGE(!) in state from the model
     * @param sensor the state from the sensor
     * @return the kalman filtered state
     */
    fun update(model: Double, sensor: Double): Double {
        u = model // Ex: change in position from odometry.
        x = x_previous + u
        p = p_previous + kalman.Q
        K = p / (p + kalman.R)
        z = sensor // Pose Estimate from April Tag / Distance Sensor
        x = x + K * (z - x)
        p = (1 - K) * p
        x_previous = x
        p_previous = p
        return x
    }

    class Vector2dKalmanFilter {
        val x: KalmanFilter
        val y: KalmanFilter

        constructor(x: KalmanFilter, y: KalmanFilter) {
            this.x = x
            this.y = y
        }

        constructor(Q: Double, R: Double) {
            x = KalmanFilter(Q, R)
            y = KalmanFilter(Q, R)
        }

        /**
         * NOTE: MODEL IS THE *CHANGE* SINCE LAST UPDATE
         */
        fun update(model: Twist2d, sensor: Pose2d): Vector2d {
            val modelPose: Pose2d = Pose2d.exp(model)
            return Vector2d(
                x.update(modelPose.position.x, sensor.position.x),
                y.update(modelPose.position.y, sensor.position.y)
            )
        }

        /**
         * NOTE: MODEL IS THE *CHANGE* SINCE LAST UPDATE
         * Probably don't switch between using a vector and a pose sensor
         */
        fun update(model: Twist2d, sensor: Vector2d): Vector2d {
            return Vector2d(
                x.update(model.line.x, sensor.x),
                y.update(model.line.y, sensor.y))
        }
    }
}