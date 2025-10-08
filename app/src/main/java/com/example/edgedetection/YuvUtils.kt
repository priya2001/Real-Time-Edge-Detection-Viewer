package com.example.edgedetection

import android.media.Image
import java.nio.ByteBuffer

object YuvUtils {
    fun yuv420ToNv21(image: Image): ByteArray {
        val width = image.width
        val height = image.height
        val ySize = width * height
        val uvSize = width * height / 4
        val nv21 = ByteArray(ySize + uvSize * 2)

        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        yBuffer.get(nv21, 0, ySize)

        val u = ByteArray(uBuffer.remaining())
        val v = ByteArray(vBuffer.remaining())
        uBuffer.get(u)
        vBuffer.get(v)

        var offset = ySize
        for (i in 0 until uvSize) {
            nv21[offset++] = v[i]
            nv21[offset++] = u[i]
        }
        return nv21
    }

    fun nv21ToRgba(nv21: ByteArray, width: Int, height: Int): ByteArray {
        val frameSize = width * height
        val rgba = ByteArray(frameSize * 4)
        var yp = 0
        for (j in 0 until height) {
            var uvp = frameSize + (j shr 1) * width
            var u = 0
            var v = 0
            for (i in 0 until width) {
                val y = (nv21[yp].toInt() and 0xff) - 16
                if ((i and 1) == 0) {
                    v = (nv21[uvp].toInt() and 0xff) - 128
                    u = (nv21[uvp + 1].toInt() and 0xff) - 128
                    uvp += 2
                }
                val y1192 = 1192 * if (y < 0) 0 else y
                var r = y1192 + 1634 * v
                var g = y1192 - 833 * v - 400 * u
                var b = y1192 + 2066 * u
                r = r.coerceIn(0, 262143)
                g = g.coerceIn(0, 262143)
                b = b.coerceIn(0, 262143)
                val idx = yp * 4
                rgba[idx] = (r shr 10).toByte()
                rgba[idx + 1] = (g shr 10).toByte()
                rgba[idx + 2] = (b shr 10).toByte()
                rgba[idx + 3] = 0xFF.toByte()
                yp++
            }
        }
        return rgba
    }
}

