package com.kikoproject.uwidget.objects

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.kikoproject.uwidget.models.User

/**
 *  Показывает аватар пользователя
 *
 *  @param user пользователь
 */
@Composable
fun Avatar(user: User, modifier: Modifier = Modifier) {
    Image(
        bitmap = user.Avatar.asImageBitmap(),
        contentDescription = "avatar",
        contentScale = ContentScale.FillBounds,
        modifier = modifier
    )
}
