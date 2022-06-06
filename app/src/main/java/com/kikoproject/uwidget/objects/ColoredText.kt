package com.kikoproject.uwidget.objects

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.models.schedules.Schedule

@Composable
fun coloredTitleText(text: String, user: User) : AnnotatedString{
    return buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.surface
            )
        ) {
            append(text.substringBefore("%"))
        }
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.primary
            )
        ) {
            var textWithoutWhite = "%"+text.substringAfter("%")
            append(textWithoutWhite.replace("%n", user.Name))
            append(textWithoutWhite.substringAfter("%n").replace("%s", user.Surname))
        }
    }
}