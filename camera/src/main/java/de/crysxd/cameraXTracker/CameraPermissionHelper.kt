package de.crysxd.cameraXTracker

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class CameraPermissionHelper : Fragment() {

    companion object {
        private const val TAG = "CameraPermissionHelper"
        private const val REQUEST_CODE = 2378
    }

    private var callback: ((Boolean) -> Unit)? = null

    fun requestCameraPermission(fm: FragmentManager, callback: (Boolean) -> Unit) {
        fm.beginTransaction().apply {
            fm.findFragmentByTag(TAG)?.let {
                remove(it)
            }
            add(this@CameraPermissionHelper, TAG)
        }.commitAllowingStateLoss()
        this.callback = callback
    }

    override fun onStart() {
        super.onStart()
        when {
            isCameraPermissionGranted(context!!) -> dispatchCallback(true)
            shouldShowRationale(activity!!) -> showRationale(context!!)
            else -> requestCameraPermission(context!!)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (REQUEST_CODE == requestCode) {
            when {
                isCameraPermissionGranted(context!!) -> dispatchCallback(true)
                shouldShowRationale(activity!!) -> showRationale(context!!)
                else -> dispatchCallback(false)
            }
        }
    }

    private fun dispatchCallback(result: Boolean) {
        callback?.invoke(result)
        callback = null
    }

    private fun showRationale(context: Context) = AlertDialog.Builder(context)
        .setMessage(getString(R.string.camera_usage_rationale))
        .setPositiveButton(getString(R.string.allow_camera_usage)) { _, _ ->
            requestCameraPermission(context)
        }
        .setCancelable(false)
        .show()

    private fun requestCameraPermission(context: Context) {
        requestPermissions(arrayOf(android.Manifest.permission.CAMERA), REQUEST_CODE)
    }

    private fun shouldShowRationale(activity: Activity) = ActivityCompat.shouldShowRequestPermissionRationale(
        activity,
        android.Manifest.permission.CAMERA
    )

    private fun isCameraPermissionGranted(context: Context) = ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

}