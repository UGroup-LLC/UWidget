package com.kikoproject.uwidget.objects.camera

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Size
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.kikoproject.uwidget.R
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
    val requestCameraPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}
    SideEffect {
        requestCameraPermission.launch(android.Manifest.permission.CAMERA)
    }
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    Box(contentAlignment = Alignment.Center) {
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
                        imageAnalysis.setAnalyzer(
                            mainExecutor,
                            BarcodeAnalyzer(context, qrCodeValue, isQrCode)
                        )

                        // Применение к провайдеру всех настроек
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, imageAnalysis, previewUseCase
                        )
                    } catch (ex: Exception) {
                        Toast.makeText(
                            context,
                            "Use case binding failed " + ex.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                previewView
            }
        )
        if (ContextCompat.checkSelfPermission(
                LocalContext.current,
                android.Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_no_camera),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}