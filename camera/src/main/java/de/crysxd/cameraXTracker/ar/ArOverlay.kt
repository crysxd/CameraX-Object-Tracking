package de.crysxd.cameraXTracker.ar

import android.graphics.Canvas

abstract class ArOverlay() {

    protected var host: ArOverlayView? = null

    abstract fun onDraw(canvas: Canvas)

    open fun onAttached(view: ArOverlayView) {
        this.host = view
    }

}