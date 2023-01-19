package com.kikoproject.uwidget.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.R

// Set of Material typography styles to start with
val monserratFamily = FontFamily(
    Font(R.font.monserrat_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.monsterrat_bold, FontWeight.Bold),
    Font(R.font.monsterrat_extrabold, FontWeight.ExtraBold),
    Font(R.font.monsterrat_semibold, FontWeight.SemiBold),
    Font(R.font.monsterrat_medium, FontWeight.Medium),
    Font(R.font.monsterrat_regular, FontWeight.Normal)
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = monserratFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    titleMedium = TextStyle(
        fontFamily = monserratFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = monserratFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        textAlign = TextAlign.Center
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontFamily = monserratFamily,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.sp
    ),
    labelLarge = TextStyle(
        fontSize = 16.sp,
        fontFamily = monserratFamily,
        fontWeight = FontWeight.SemiBold
    ),
    labelMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = monserratFamily,
        fontWeight = FontWeight.Medium
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
     */
)