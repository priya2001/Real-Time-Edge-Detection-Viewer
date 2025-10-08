package com.example.edgedetection

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.TextureView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private lateinit var textureView: TextureView
    private lateinit var toggleButton: Button
    private lateinit var cameraHelper: Camera2Helper
    private lateinit var glRenderer: GLRenderer
    private var showEdges = true

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textureView = findViewById(R.id.textureView)
        toggleButton = findViewById(R.id.btnToggle)
        glRenderer = GLRenderer(this)

        cameraHelper = Camera2Helper(this, object : Camera2Helper.FrameListener {
            override fun onFrameAvailable(rgba: ByteArray, width: Int, height: Int) {
                val processed = if (showEdges) {
                    NativeBridge.processFrame(rgba, width, height)
                } else rgba
                glRenderer.updateFrame(processed, width, height)
            }
        })

        toggleButton.setOnClickListener {
            showEdges = !showEdges
            toggleButton.text = if (showEdges) "Show Raw" else "Show Edges"
        }

        requestCameraPermission()
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 101)
        } else startCamera()
    }

    private fun startCamera() {
        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, w: Int, h: Int) {
                cameraHelper.openCamera(textureView)
            }
            override fun onSurfaceTextureSizeChanged(s: SurfaceTexture, w: Int, h: Int) {}
            override fun onSurfaceTextureDestroyed(s: SurfaceTexture): Boolean = true
            override fun onSurfaceTextureUpdated(s: SurfaceTexture) {}
        }
    }
}

