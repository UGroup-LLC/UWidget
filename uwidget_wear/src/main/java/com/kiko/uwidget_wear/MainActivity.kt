package com.kiko.uwidget_wear

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.kiko.uwidget_wear.objects.text.colorize
import com.kiko.uwidget_wear.ui.theme.UWidgetTheme

var prefs: SharedPreferences? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UWidgetTheme {
                val context = LocalContext.current
                prefs = this.getSharedPreferences(
                    context.packageName, Context.MODE_PRIVATE
                )
                WearApp()
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun WearApp() {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (checkConfigured()) {
        } else {
            Text(text = "Здравствуйте!", style = MaterialTheme.typography.caption3)
            Text(
                text = "U@Widget@ for @WearOS@".colorize(),
                style = MaterialTheme.typography.caption1,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

fun checkConfigured(): Boolean {
    return prefs!!.getBoolean("configured", false)
}
