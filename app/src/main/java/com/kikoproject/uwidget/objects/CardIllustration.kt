package com.kikoproject.uwidget.objects

import android.graphics.ColorSpace
import android.widget.ImageView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardIllustration(
    drawable: Int,
    primaryKeys: Int,
    secondaryKeys: Int,
    color: Color = MaterialTheme.colors.primary,
    secColor: Color = MaterialTheme.colors.secondary,
    border: BorderStroke = BorderStroke(2.dp, color = MaterialTheme.colors.surface.copy(0.4f))
) {
    Box(contentAlignment = Alignment.TopCenter) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            border = border,
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            val context = LocalContext.current

            val imgRes = ImageView(context)
            val vector = VectorChildFinder(context, drawable, imgRes)

            for (i in 1..primaryKeys) {
                vector.findPathByName("p$i").fillColor = android.graphics.Color.argb(
                    color.toArgb().alpha,
                    color.toArgb().red,
                    color.toArgb().green,
                    color.toArgb().blue
                )
            }
            for (i in 1..secondaryKeys) {
                vector.findPathByName("s$i").fillColor = android.graphics.Color.argb(
                    secColor.toArgb().alpha,
                    secColor.toArgb().red,
                    secColor.toArgb().green,
                    secColor.toArgb().blue
                )
            }


            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(20.dp)) {

                Image(
                    alignment = Alignment.Center,
                    bitmap = imgRes.drawable.toBitmap().asImageBitmap(),
                    contentDescription = null
                )
            }
        }
    }
}