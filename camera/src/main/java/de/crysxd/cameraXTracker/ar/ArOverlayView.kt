package de.crysxd.cameraXTracker.ar

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.GuardedBy

class ArOverlayView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, style: Int = 0) :
    FrameLayout(context, attributeSet, style) {

    private val overlays = mutableListOf<ArOverlay>()
    private val objectLock = Object()

    init {
        setWillNotDraw(false)
    }

    @GuardedBy("objectLock")
    fun remove(overlay: ArOverlay) {
        overlays.remove(overlay)
    }

    @GuardedBy("objectLock")
    fun add(overlay: ArOverlay) {
        remove(overlay)
        overlays.add(overlay)
        overlay.onAttached(this)
        invalidate()
    }

    @GuardedBy("objectLock")
    fun clear() {
        overlays.clear()
        invalidate()
    }

    @GuardedBy("objectLock")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        overlays.forEach {
            it.onDraw(canvas)
        }
    }
}