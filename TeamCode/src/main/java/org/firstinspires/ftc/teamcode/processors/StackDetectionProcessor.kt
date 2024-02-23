package org.firstinspires.ftc.teamcode.processors

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import org.firstinspires.ftc.robotcore.external.function.Consumer
import org.firstinspires.ftc.robotcore.external.function.Continuation
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import java.util.Locale
import java.util.concurrent.atomic.AtomicReference
import java.util.function.DoubleSupplier

class StackDetectionProcessor(// upper bounds for masking
	private val lower: Scalar, // lower bounds for masking
	private val upper: Scalar, private val minArea: DoubleSupplier, private val partition: DoubleSupplier) : VisionProcessor, CameraStreamSource {
	private val textPaint: TextPaint
	private val linePaint: Paint
	private val contours: ArrayList<MatOfPoint>
	private val hierarchy = Mat()
	private val sel1 = Mat() // these facilitate capturing through 0
	private val sel2 = Mat()

	/**
	 * @return the x position of the currently found largest contour in the range [0, camera width], or -1 if no largest contour has been determined
	 */
	var largestContourX = 0.0
		private set

	/**
	 * @return the y position of the currently found largest contour in the range [0, camera height], or -1 if no largest contour has been determined
	 */
	var largestContourY = 0.0
		private set

	/**
	 * @return the area of the currently found largest contour, or -1 if no largest contour has been determined
	 */
	var largestContourArea = 0.0

	// returns the largest contour if you want to get information about it
	var largestContour: MatOfPoint? = null
		private set
	private var previousPropPosition: PropPositions? = null

	/**
	 * @return the last found prop position, if none have been found, returns [PropPositions.Unfound]
	 */
	var recordedPropPosition = PropPositions.Unfound
		private set
	private val lastFrame = AtomicReference(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565))

	/**
	 * Uses HSVs for the scalars
	 *
	 * @param lower   the lower masked bound, a three a value scalar in the form of a HSV
	 * @param upper   the upper masked bound, a three a value scalar in the form of a HSV
	 * @param minArea the minimum area for a detected blob to be considered the prop
	 * @param partition    the dividing point for the prop to be on the left
	 */
	init {
		contours = ArrayList()

		// setting up the paint for the text in the center of the box
		textPaint = TextPaint()
		textPaint.color = Color.GREEN // you may want to change this
		textPaint.textAlign = Paint.Align.CENTER
		textPaint.isAntiAlias = true
		textPaint.textSize = 40f // or this
		textPaint.setTypeface(Typeface.DEFAULT_BOLD)

		// setting up the paint for the lines that comprise the box
		linePaint = Paint()
		linePaint.color = Color.GREEN // you may want to change this
		linePaint.isAntiAlias = true
		linePaint.strokeWidth = 10f // or this
		linePaint.strokeCap = Paint.Cap.ROUND
		linePaint.strokeJoin = Paint.Join.ROUND
	}

	override fun init(width: Int, height: Int, calibration: CameraCalibration) {
		lastFrame.set(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565))
		// this method comes with all VisionProcessors, we just don't need to do anything here, and you dont need to call it
	}

	override fun processFrame(frame: Mat, captureTimeNanos: Long): Any {
		// this method processes the image (frame) taken by the camera, and tries to find a suitable prop
		// you dont need to call it
		Core.rotate(frame, frame, Core.ROTATE_180)

		// this converts the frame from RGB to HSV, which is supposed to be better for doing colour blob detection
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2HSV)
		// thats why you need to give your scalar upper and lower bounds as HSV values
		if (upper.`val`[0] < lower.`val`[0]) {
			// makes new scalars for the upper [upper, 0] detection, places the result in sel1
			Core.inRange(frame, Scalar(upper.`val`[0], lower.`val`[1], lower.`val`[2]), Scalar(0.0, upper.`val`[1], upper.`val`[2]), sel1)
			// makes new scalars for the lower [0, lower] detection, places the result in sel2
			Core.inRange(frame, Scalar(0.0, lower.`val`[1], lower.`val`[2]), Scalar(lower.`val`[0], upper.`val`[1], upper.`val`[2]), sel2)

			// combines the selections
			Core.bitwise_or(sel1, sel2, frame)
		} else {
			// this process is simpler if we are not trying to wrap through 0
			// this method makes the colour image black and white, with everything between your upper and lower bound values as white, and everything else black
			Core.inRange(frame, lower, upper, frame)
		}


		// this empties out the list of found contours, otherwise we would keep all the old ones, read on to find out more about contours!
		contours.clear()

		// this finds the contours, which are borders between black and white, and tries to simplify them to make nice outlines around potential objects
		// this basically helps us to find all the shapes/outlines of objects that exist within our colour range
		Imgproc.findContours(frame, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)

		// this sets up our largest contour area to be 0
		largestContourArea = -1.0
		// and our currently found largest contour to be null
		largestContour = null

		// gets the current minimum area from min area
		val minArea = minArea.asDouble

		// finds the largest contour!
		// for each contour we found before we loop over them, calculate their area,
		// and then if our area is larger than our minimum area, and our currently found largest area
		// it stores the contour as our largest contour and the area as our largest area
		for (contour in contours) {
			val area = Imgproc.contourArea(contour)
			if (area > largestContourArea && area > minArea) {
				largestContour = contour
				largestContourArea = area
			}
		}


		// sets up the center points of our largest contour to be -1 (offscreen)
		largestContourY = -1.0
		largestContourX = largestContourY

		// if we found it, calculates the actual centers
		if (largestContour != null) {
			val moment = Imgproc.moments(largestContour)
			largestContourX = moment.m10 / moment.m00
			largestContourY = moment.m01 / moment.m00
		}

		// determines the current prop position, using the left and right dividers we gave earlier
		// if we didn't find any contours which were large enough, sets it to be unfound
		val propPosition: PropPositions
		propPosition = if (largestContour == null) {
			PropPositions.Left
		} else if (largestContourX <= partition.asDouble) {
			PropPositions.Middle
		} else if (largestContourX >= partition.asDouble) {
			PropPositions.Right
		} else {
			PropPositions.Left
		}

		// if we have found a new prop position, and it is not unfound, updates the recorded position,
		// this makes sure that if our camera is playing up, we only need to see the prop in the correct position
		// and we will hold onto it
		if (propPosition != previousPropPosition) {
			recordedPropPosition = propPosition
		}

		// updates the previous prop position to help us check for changes
		previousPropPosition = propPosition

//		Imgproc.drawContours(frame, contours, -1, colour);

		// returns back the edited image, don't worry about this too much
		val b = Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.RGB_565)
		Utils.matToBitmap(frame, b)
		lastFrame.set(b)
		return frame
	}

	override fun onDrawFrame(canvas: Canvas, onscreenWidth: Int, onscreenHeight: Int, scaleBmpPxToCanvasPx: Float, scaleCanvasDensity: Float, userContext: Any) {
		// this method draws the rectangle around the largest contour and puts the current prop position into that rectangle
		// you don't need to call it

//		for (MatOfPoint contour : contours) {
//			Rect rect = Imgproc.boundingRect(contour);
//			canvas.drawLines(new float[]{rect.x * scaleBmpPxToCanvasPx, rect.y * scaleBmpPxToCanvasPx, (rect.x + rect.width) * scaleBmpPxToCanvasPx, (rect.y + rect.height) * scaleBmpPxToCanvasPx}, textPaint);
//		}

		// if the contour exists, draw a rectangle around it and put its position in the middle of the rectangle
		if (largestContour != null) {
			val rect = Imgproc.boundingRect(largestContour)
			val points = floatArrayOf(rect.x * scaleBmpPxToCanvasPx, rect.y * scaleBmpPxToCanvasPx, (rect.x + rect.width) * scaleBmpPxToCanvasPx, (rect.y + rect.height) * scaleBmpPxToCanvasPx)
			canvas.drawLine(points[0], points[1], points[0], points[3], linePaint)
			canvas.drawLine(points[0], points[1], points[2], points[1], linePaint)
			canvas.drawLine(points[0], points[3], points[2], points[3], linePaint)
			canvas.drawLine(points[2], points[1], points[2], points[3], linePaint)
			val text = String.format(Locale.ENGLISH, "%s", recordedPropPosition.toString())
			canvas.drawText(text, largestContourX.toFloat() * scaleBmpPxToCanvasPx, largestContourY.toFloat() * scaleBmpPxToCanvasPx, textPaint)
		}
	}

	fun close() {
		hierarchy.release()
		sel1.release()
		sel2.release()
	}

	// the enum that stores the 4 possible prop positions
	enum class PropPositions {
		Left,
		Middle,
		Right,
		Unfound
	}

	override fun getFrameBitmap(continuation: Continuation<out Consumer<Bitmap>?>) {
		continuation.dispatch { bitmapConsumer: Consumer<Bitmap>? -> bitmapConsumer!!.accept(lastFrame.get()) }
	}
}