package de.crysxd.cameraXTracker.ar

open class ArObjectTracker() {

    private val trackingListeners = mutableListOf<ArObjectTrackingListener>()
    private var pipedTracked: ArObjectTracker? = null

    fun addTrackingListener(listener: ArObjectTrackingListener): ArObjectTracker {
        trackingListeners.add(listener)
        return this
    }

    fun addTrackingListener(listener: (ArObject?) -> Unit): ArObjectTracker {
        addTrackingListener(object : ArObjectTrackingListener {
            override fun onObjectTracked(arObject: ArObject?) {
                listener(arObject)
            }
        })
        return this
    }

    fun removeTrackingListener(listener: ArObjectTrackingListener) = trackingListeners.remove(listener)

    fun pipe(otherTracker: ArObjectTracker): ArObjectTracker {
        pipedTracked = otherTracker
        return otherTracker
    }

    fun clearListenersAndPipe() {
        trackingListeners.clear()
        pipedTracked?.clearListenersAndPipe()
        pipedTracked = null
    }

    open fun processObject(arObject: ArObject?) {
        publish(arObject)
    }

    protected fun publish(arObject: ArObject?) {
        // Pipe
        pipedTracked?.processObject(arObject)

        // Notify
        trackingListeners.forEach {
            it.onObjectTracked(arObject)
        }
    }

    interface ArObjectTrackingListener {
        fun onObjectTracked(arObject: ArObject?)
    }
}