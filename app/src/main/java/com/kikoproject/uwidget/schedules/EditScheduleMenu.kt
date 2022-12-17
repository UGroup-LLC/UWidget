package com.kikoproject.uwidget.schedules

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.QrErrorCorrectionLevel
import com.github.alexzhirkevich.customqrgenerator.style.DrawableSource
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.QrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.*
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.objects.BackHeader
import com.kikoproject.uwidget.objects.buttons.StandardButton
import com.kikoproject.uwidget.utils.toStandardColor

/**
 * Админ панель в расписании содержащяя в себе редактирование расписание, удаление пользователей
 */
@Preview(showBackground = true)
@Composable
fun EditScheduleMenu() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 30.dp, vertical = 10.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            BackHeader("Админ панель")
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    StandardButton(
                        content = { navController.navigate(ScreenNav.EditScheduleNav.route) },
                        text = "Редактировать",
                        icon = painterResource(R.drawable.ic_pencil)
                    )
                }
                item {
                    StandardButton(
                        content = { navController.navigate(ScreenNav.EditMembersNav.route) },
                        text = "Управление участниками",
                        icon = painterResource(R.drawable.ic_account_circle)
                    )
                }
                item {
                    if (curSchedule != null) {
                        if (curSchedule?.JoinCode.toString() != "null") ShowJoinCode()
                    }
                }
                item {
                    ShowQRJoinCode()
                }
            }
        }
    }
}
