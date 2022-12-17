package com.kikoproject.uwidget.analyzers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.MutableState
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(
    val context: Context,
    val rBarcode: MutableState<String>,
    val isQrCode: MutableState<Boolean>
) : ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeOptInUsageError")
    // Анализируем полученное изображение здесь
    override fun analyze(imageProxy: ImageProxy) {
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE,
                ).build()
            val scanner = BarcodeScanning.getClient()
            val result = scanner.process(image)
                // Успешно выполнено сканирование и найдено штрихкодов
                .addOnSuccessListener { barcodes ->
                    barcodes.forEach { barcode ->
                        Log.d("QR", barcode.displayValue.toString())
                        rBarcode.value = barcode.displayValue.toString()
                        isQrCode.value = true
                    }
                }
                // Ошибка выполнения
                .addOnFailureListener {
                    Log.d("BARCODE", it.message.toString())
                }
                .addOnCompleteListener {
                    mediaImage.close();
                    imageProxy.close();
                }
        }
    }
}
