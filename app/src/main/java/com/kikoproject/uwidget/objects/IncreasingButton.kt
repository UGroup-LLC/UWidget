package com.kikoproject.uwidget.objects

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.ui.theme.themeTextColor

@Composable
fun IncreaseButtons(
    texts: List<String>,
    roundStrength: Float = 20f,
    inactiveColor: Color,
    activeColor: Color,
    fontSize: TextUnit = 12.sp
) : Int{
    val activeButtonIndex = remember { mutableStateOf(0) }

    LazyRow() {
        itemsIndexed(texts) { index, text ->

            var leftStrength = roundStrength
            var rightStrength = roundStrength

            when (index) {
                0 -> rightStrength = 0f
                texts.lastIndex -> leftStrength = 0f
                else -> {
                    rightStrength = 0f
                    leftStrength = 0f
                }
            }// Делаем правильные закругления

            Button(
                onClick = {
                    activeButtonIndex.value = index
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = if (index != activeButtonIndex.value) inactiveColor else activeColor),
                elevation = ButtonDefaults.elevation(0.dp,0.dp,0.dp),
                shape = RoundedCornerShape(
                    topStart = leftStrength,
                    bottomStart = leftStrength,
                    topEnd = rightStrength,
                    bottomEnd = rightStrength
                )
            ) {
                Text(text = text, color = themeTextColor(), fontSize = fontSize)
            }
        }
    }

    return activeButtonIndex.value
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun PreviewIncreaseButtons() {
    IncreaseButtons(
        listOf("list", "kok", "notkok"),
        20f,
        Color.White, Color.Red,
    )
}