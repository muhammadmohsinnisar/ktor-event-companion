package com.mohsin.utils

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import java.io.ByteArrayOutputStream
import java.util.*

object QrCodeGenerator {
    internal fun generateQrCode(content: String, width: Int = 300, height: Int = 300): ByteArray {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height)
        val outputStream = ByteArrayOutputStream()
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream)
        return outputStream.toByteArray()
    }

    fun generateQrCodeAsBase64(content: String): String {
        val qrBytes = generateQrCode(content)
        return Base64.getEncoder().encodeToString(qrBytes)
    }
}
