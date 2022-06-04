package com.kikoproject.uwidget.objects

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun coloredText(first: String, second: String) : AnnotatedString{
    return buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.surface
            )
        ) {
            append(first)
        }
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.primary
            )
        ) {
            append(second)
        }
    }
}