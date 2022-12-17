package com.kikoproject.uwidget.objects.camera

import android.annotation.SuppressLint
import android.util.Size
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.kikoproject.uwidget.analyzers.BarcodeAnalyzer
import kotlinx.coroutines.launch

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun JoinQRCodeScanner(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    isQrCode: MutableState<Boolean>,
    qrCodeValue: MutableState<String> // Возвращает полученный штрихкод
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }


            val previewUseCase = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val mainExecutor = ContextCompat.getMainExecutor(context)

            coroutineScope.launch {
                val cameraProvider = cameraProviderFuture.get()
                try {
                    cameraProvider.unbindAll()

                    // Создание анализа изображения
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setTargetResolution(Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                    imageAnalysis.setAnalyzer(mainExecutor, BarcodeAnalyzer(context, qrCodeValue, isQrCode))

                    // Применение к провайдеру всех настроек
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector,imageAnalysis, previewUseCase
                    )
                } catch (ex: Exception) {
                    Toast.makeText(context,"Use case binding failed " + ex.message, Toast.LENGTH_SHORT).show()
                }
            }

            previewView
        }
    )
}