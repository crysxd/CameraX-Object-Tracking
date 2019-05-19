package de.crysxd.cameraXTracker.ar

import android.graphics.Canvas
import android.view.View

class ViewArOverlay(private val view: View) : ArOverlay(), ArObjectTracker.ArObjectTrackingListener {

    override fun onAttached(view: ArOverlayView) {
        host?.removeView(this.view)
        super.onAttached(view)
        host?.addView(this.view)
    }

    override fun onObjectTracked(arObject: ArObject?) {
        if (arObject == null) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
            view.translationX = arObject.boundingBox.centerX() - view.width / 2
            view.translationY = arObject.boundingBox.centerY() - view.height / 2
        }
    }

    override fun onDraw(canvas: Canvas) = Unit

}