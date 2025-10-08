package com.example.edgedetection

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.util.Log
import android.view.TextureView

class Camera2Helper(
    private val activity: Activity,
    private val frameListener: FrameListener
) {
    interface FrameListener {
        fun onFrameAvailable(rgba: ByteArray, width: Int, height: Int)
    }

    private val cameraManager =
        activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var cameraDevice: CameraDevice? = null
    private var imageReader: ImageReader? = null

    @SuppressLint("MissingPermission")
    fun openCamera(textureView: TextureView) {
        val cameraId = cameraManager.cameraIdList.first()
        val characteristics = cameraManager.getCameraCharacteristics(cameraId)
        val size = characteristics.get(
            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
        )!!.getOutputSizes(ImageFormat.YUV_420_888)[0]

        imageReader = ImageReader.newInstance(size.width, size.height, ImageFormat.YUV_420_888, 2)
        imageReader!!.setOnImageAvailableListener({ reader ->
            val image = reader.acquireLatestImage() ?: return@setOnImageAvailableListener
            val nv21 = YuvUtils.yuv420ToNv21(image)
            val rgba = YuvUtils.nv21ToRgba(nv21, image.width, image.height)
            frameListener.onFrameAvailable(rgba, image.width, image.height)
            image.close()
        }, null)

        cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                val captureRequest = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequest.addTarget(imageReader!!.surface)
                camera.createCaptureSession(
                    listOf(imageReader!!.surface),
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(session: CameraCaptureSession) {
                            session.setRepeatingRequest(captureRequest.build(), null, null)
                        }
                        override fun onConfigureFailed(session: CameraCaptureSession) {
                            Log.e("Camera2Helper", "Config failed")
                        }
                    },
                    null
                )
            }

            override fun onDisconnected(camera: CameraDevice) {
                camera.close()
            }

            override fun onError(camera: CameraDevice, error: Int) {
                camera.close()
            }
        }, null)
    }
}

