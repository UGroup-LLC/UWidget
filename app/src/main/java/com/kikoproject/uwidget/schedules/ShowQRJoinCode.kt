package com.kikoproject.uwidget.schedules

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.QrErrorCorrectionLevel
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.QrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.*
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.utils.toStandardColor


@Composable
fun ShowQRJoinCode() {
    val context = LocalContext.current
    val data = QrData.Text(curSchedule?.JoinCode.toString())
    val options = QrVectorOptions.Builder()
        .setErrorCorrectionLevel(QrErrorCorrectionLevel.High)
        .setColors(
            QrVectorColors(
                dark = QrVectorColor.Solid(MaterialTheme.colors.secondary.toStandardColor()),
                ball = QrVectorColor.Solid(MaterialTheme.colors.secondary.toStandardColor()),
                frame = QrVectorColor.Solid(MaterialTheme.colors.primary.toStandardColor())
            )
        )
        .setBackground(QrVectorBackground())
        .setShapes(
            QrVectorShapes(
                darkPixel = QrVectorPixelShape
                    .RoundCorners(.5f),
                ball = QrVectorBallShape.Circle(1f),
                frame = QrVectorFrameShape
                    .RoundCorners(.25f),
            )
        )
        .build()
    val qrDrawable = QrCodeDrawable(context, data, options)
    Card(
        shape = RoundedCornerShape(15.dp),
        contentColor = MaterialTheme.colors.background,
        backgroundColor = MaterialTheme.colors.background,
        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
        modifier = Modifier.padding(horizontal = 30.dp, vertical = 0.dp)
    ) {
        Column() {
            Image(
                bitmap = qrDrawable.toBitmap(1024, 1024).asImageBitmap(),
                contentDescription = "qrCode",
                modifier = Modifier.padding(30.dp)
            )
        }
    }
}

