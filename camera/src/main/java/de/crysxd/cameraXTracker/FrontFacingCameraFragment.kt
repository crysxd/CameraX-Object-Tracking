package de.crysxd.cameraXTracker

import androidx.camera.core.CameraX
import androidx.camera.core.ImageAnalysisConfig
import androidx.camera.core.PreviewConfig

class FrontFacingCameraFragment : CameraFragment() {

    override fun onCreateAnalyzerConfigBuilder(): ImageAnalysisConfig.Builder = super.onCreateAnalyzerConfigBuilder().apply {
        setLensFacing(CameraX.LensFacing.FRONT)
    }

    override fun onCreatePreivewConfigBuilder(): PreviewConfig.Builder = super.onCreatePreivewConfigBuilder().apply {
        setLensFacing(CameraX.LensFacing.FRONT)
    }
}