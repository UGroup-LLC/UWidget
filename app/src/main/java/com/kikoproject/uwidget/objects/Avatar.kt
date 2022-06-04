package com.kikoproject.uwidget.objects

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kikoproject.uwidget.main.curUser
import com.kikoproject.uwidget.main.db
import com.kikoproject.uwidget.networking.AvatarResult
import com.kikoproject.uwidget.networking.getUserBitmap

@Composable
fun Avatar(account: GoogleSignInAccount, modifier: Modifier) {

    if (account.id != null) {
        Image(
            bitmap = curUser.Avatar.asImageBitmap(),
            contentDescription = "avatar",
            contentScale = ContentScale.FillBounds,
            modifier = modifier
        )
    }
}