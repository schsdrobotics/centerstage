import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.imgproc.Imgproc

class TeamPropDetector : VisionProcessor {
    var contours = mutableListOf<MatOfPoint>()
    var thresh = Mat()
    var mono = Mat()

    override fun init(width: Int, height: Int, calibration: CameraCalibration?) { }

    override fun processFrame(frame: Mat?, captureTimeNanos: Long) {
        thresh.release()
        mono.release()

        Imgproc.cvtColor(frame, mono, Imgproc.COLOR_RGB2GRAY)
        Imgproc.findContours(mono, contours, Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)
    }

    override fun onDrawFrame(
        canvas: Canvas?,
        onscreenWidth: Int,
        onscreenHeight: Int,
        scaleBmpPxToCanvasPx: Float,
        scaleCanvasDensity: Float,
        userContext: Any?
    ) {
        val red = Paint()
        red.color = Color.RED

        val rect = contours.map { Imgproc.boundingRect(it) }
        rect.map { Rect(it.x, it.y, it.x + it.width, it.y + it.height) }
            .map { canvas?.drawRect(it, red)}
    }

}