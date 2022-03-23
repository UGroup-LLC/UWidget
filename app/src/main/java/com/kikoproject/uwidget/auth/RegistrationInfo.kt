package com.kikoproject.uwidget.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kikoproject.uwidget.ui.theme.UWidgetTheme
import com.kikoproject.uwidget.ui.theme.themeTextColor

@Composable
fun RegisterScreen() {
    UWidgetTheme() {
        val textColor = themeTextColor()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            val state = remember { mutableStateOf(TextFieldValue(text = "")) }
            OutlinedTextField(
                value = state.value,
                onValueChange = { state.value = it },
                label = { Text(text = "Введите отображаемое имя", color = textColor.copy(alpha = 0.4f)) },
                shape = RoundedCornerShape(16.dp),
                textStyle = TextStyle(color = textColor)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterScreen()
}