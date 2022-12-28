package com.kikoproject.uwidget.objects

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier 
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.radusalagean.infobarcompose.BaseInfoBarMessage

class CustomToastBar(
    val text: String,
    val icon: ImageVector = Icons.Rounded.Info,
    val materialColor: ColorScheme,
    val iconColor: Color = materialColor.primary,
    val textColor: Color = materialColor.onSurface,
    override val backgroundColor: Color = Color.Black,
    override val displayTimeSeconds: Int? = 2,
) : BaseInfoBarMessage() {
    override val containsControls: Boolean = false
}

/**
 * Кастомный всплывающий диалог
 *
 * @author Kiko
 */
@Composable
fun customToastBarMessage() : @Composable (CustomToastBar) -> Unit {
    val content: @Composable (CustomToastBar) -> Unit = { message ->

        Row {
            Icon(
                modifier = Modifier.padding(16.dp).align(Alignment.CenterVertically),
                imageVector = message.icon,
                contentDescription = null,
                tint = message.iconColor
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = message.text,
                color = message.textColor
            )
        }
    }
    return content
}