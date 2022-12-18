package com.kikoproject.uwidget.objects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.curUser
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.main.roomDb
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.ui.theme.MainThemes
import com.kikoproject.uwidget.ui.theme.systemThemeIsEnabled
import com.kikoproject.uwidget.ui.theme.themeAppMode

/**
 * Хэдэр Dashboard, содержит в себе текст заголовка, тему приложения и аватар пользователя
 * @param account Google аккаунт пользователя
 * @author Kiko
 */
@Composable
fun MainHeader(account: GoogleSignInAccount?) {
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top) {
        if (account != null) {
            Avatar(
                user = curUser, modifier = Modifier
                    .requiredSize(36.dp)
                    .clip(CircleShape)
                    .weight(2f)
                    .clickable {
                        navController.navigate(ScreenNav.GoogleAuthNav.route)
                    }
            )
        }
        Text(
            text = "Главная",
            modifier = Modifier
                .weight(10f),
            color = MaterialTheme.colors.surface,
            style = MaterialTheme.typography.h6
        )


        val icon = if (systemThemeIsEnabled.value) {
            if (themeAppMode.value == MainThemes.DARK.value) { // Если темный режим выбираем иконку луны
                R.drawable.ic_moon
            } else {
                R.drawable.ic_lightmode
            }
        } else {// Если включена кастом тема
            R.drawable.ic_custom_theme
        }


        IconButton(
            onClick = {
                themeAppMode.value = !themeAppMode.value
                systemThemeIsEnabled.value = true
                val newGenOpt = roomDb!!.optionsDao().get()
                    .copy(Theme = themeAppMode.value, IsSystemColors = systemThemeIsEnabled.value)
                roomDb!!.optionsDao().updateOption(newGenOpt)
            }, modifier = Modifier
                .weight(2f)
                .requiredSize(36.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color(0xFFBF842C),
                modifier = Modifier.requiredSize(24.dp)
            )
        }
    }
}

/**
 * Хедер окна (верх окна) содержит в себе кнопку назад а так же заголовок
 *
 * @param text текст заголовка
 *
 * @author Kiko
 */
@Composable
fun BackHeader(text: String) {
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top) {
        IconButton(
            onClick = { navController.popBackStack() }, modifier = Modifier
                .weight(2f)
                .requiredSize(36.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.requiredSize(24.dp)
            )
        }
        Text(
            text = text,
            modifier = Modifier
                .weight(10f),
            color = MaterialTheme.colors.surface,
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.requiredSize(24.dp))
    }
}