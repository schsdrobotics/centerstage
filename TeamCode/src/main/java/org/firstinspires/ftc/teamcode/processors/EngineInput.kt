package org.firstinspires.ftc.teamcode.processors

import android.graphics.Canvas
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class EngineInput : VisionProcessor {
    lateinit var imgGray: Mat

    override fun init(p0: Int, p1: Int, p2: CameraCalibration?) {
        TODO("Not yet implemented")
    }

    override fun processFrame(frame: Mat?, captureTimeNanos: Long) {
        Imgproc.cvtColor(frame, imgGray, Imgproc.COLOR_RGB2GRAY)
        
    }

    override fun onDrawFrame(p0: Canvas?, p1: Int, p2: Int, p3: Float, p4: Float, p5: Any?) {
        TODO("Not yet implemented")
    }
}