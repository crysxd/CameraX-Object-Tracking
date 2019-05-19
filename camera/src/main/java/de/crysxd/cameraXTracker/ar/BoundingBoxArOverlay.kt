package de.crysxd.cameraXTracker.ar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.annotation.ColorInt
import de.crysxd.cameraXTracker.BuildConfig
import de.crysxd.cameraXTracker.R


class BoundingBoxArOverlay(private val context: Context, private val debugMode: Boolean = false) : ArOverlay(), ArObjectTracker.ArObjectTrackingListener {

    private val cornerRadius = context.resources.getDimension(R.dimen.ar_bounding_box_corner_radius)
    private val boxPadding = context.resources.getDimension(R.dimen.ar_bounding_box_padding)
    private var currentBoundingBox: RectF? = null
    private var currentTrackingId: Int? = null

    private val boundingBoxBorderPaint = Paint().apply {
        strokeWidth = context.resources.getDimension(R.dimen.ar_bounding_rect_stroke_width)
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val boundingBoxFillPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = cornerRadius
        isAntiAlias = true
    }

    init {
        setBoundingBoxColor(Color.WHITE)
    }

    override fun onObjectTracked(arObject: ArObject?) {
        currentBoundingBox = arObject?.boundingBox
        currentTrackingId = arObject?.trackingId

        // Add padding in all directions to the bounding box
        if (currentBoundingBox != null) {
            currentBoundingBox!!.left -= boxPadding
            currentBoundingBox!!.top -= boxPadding
            currentBoundingBox!!.right += boxPadding
            currentBoundingBox!!.bottom += boxPadding
        }

        host?.invalidate()
    }

    fun setBoundingBoxColor(@ColorInt color: Int) {
        boundingBoxBorderPaint.color = color
        boundingBoxBorderPaint.alpha = 153
        boundingBoxFillPaint.color = color
        boundingBoxFillPaint.alpha = 51
        host?.invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (currentBoundingBox != null) {
            canvas.drawRoundRect(currentBoundingBox!!, cornerRadius, cornerRadius, boundingBoxFillPaint)
            canvas.drawRoundRect(currentBoundingBox!!, cornerRadius, cornerRadius, boundingBoxBorderPaint)

            if (debugMode) {
                canvas.drawText(
                    "$currentTrackingId",
                    currentBoundingBox!!.left + cornerRadius,
                    currentBoundingBox!!.bottom - cornerRadius,
                    textPaint
                )
            }
        }
    }
}