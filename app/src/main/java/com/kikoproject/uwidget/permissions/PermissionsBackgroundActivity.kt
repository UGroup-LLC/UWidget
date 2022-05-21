package com.kikoproject.uwidget.permissions

import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.navigation.PermissionNav
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.ui.theme.themeTextColor



@Composable
fun BackgroundPermissionActivity() {
    val textColor = MaterialTheme.colors.surface
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Необходимо разрешить\nфоновую активность",
                color = textColor,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 15.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_undraw_permission_unlock),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(0.3f)
            )
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 40.dp, vertical = 10.dp)
                    .fillMaxWidth(0.9f),
                onClick = {
                    val packageName = context.packageName
                    val powerManager = context.getSystemService(POWER_SERVICE) as PowerManager;
                    val powerIntent = Intent()
                    if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                        powerIntent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                        powerIntent.data = Uri.parse("package:$packageName")
                        context.startActivity(powerIntent)
                    }
                    else{ // Battery optimization off
                        navController.navigate(ScreenNav.Dashboard.route)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colors.primary.copy(
                        alpha = 0.2f
                    )
                ),
                border = BorderStroke(
                    1.dp,
                    color = MaterialTheme.colors.primary
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.weight(1f),
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Permission background allow",
                        tint = textColor
                    )
                    Text(
                        "Разрешить",
                        modifier = Modifier.weight(10f),
                        color = textColor,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewBackgroundActivity() {
    BackgroundPermissionActivity()
}