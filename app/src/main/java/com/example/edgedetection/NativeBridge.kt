package com.example.edgedetection

object NativeBridge {
    external fun processFrame(rgba: ByteArray, width: Int, height: Int): ByteArray
}

