package com.kikoproject.uwidget.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.ui.theme.UWidgetTheme

@Composable
fun GoogleAuthScreen() {
    UWidgetTheme() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {

                val textColor = if (isSystemInDarkTheme()) {
                    Color(0xFFEDF2F8)
                } else {
                    Color(0xFF000000)
                }

                val colorTextAnnotation = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = textColor
                        )
                    ) {
                        append("Weclome to U")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF84B7F9)
                        )
                    ) {
                        append("Widget")
                    }
                }

                Text(
                    text = colorTextAnnotation,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 20.dp, start = 30.dp),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = textColor
                )
                Text(
                    text = "Продолжая, вы соглашаетесь с пользовательским соглашением и с политикой конфидициальности",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 20.dp, start = 30.dp, end = 30.dp),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    color = textColor
                )
                Divider(
                    color = textColor.copy(alpha = 0.2f),
                    modifier = Modifier.padding(top = 20.dp, start = 30.dp, end = 30.dp)
                )
                Text(
                    text = "Войдите в ваш Google аккаунт для продолжения",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 20.dp, start = 50.dp, end = 50.dp),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = textColor
                )
                Text(
                    text = "Вход в аккаунт Google будет использован UWidget для:\n• Индексации другими пользователями вашего аккаунта\n• Индексации вашего аккаунта системой\n• Создания публичных расписаний",
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(top = 20.dp, start = 30.dp, end = 30.dp),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    color = textColor.copy(alpha = 0.8f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            modifier = Modifier.padding(top = 30.dp),
                            onClick = {},
                            border = BorderStroke(
                                1.dp,
                                color = Color(0xBE347CE9)
                            ),
                            colors = ButtonDefaults.buttonColors(Color(0x3B3F81E6))
                        ) {
                            Text(
                                text = "Войти с помощью Google",
                                color = Color(0xFF347CE9)
                            )
                        }
                        OutlinedButton(
                            modifier = Modifier.padding(top = 10.dp),
                            onClick = {},
                            border = BorderStroke(
                                1.dp,
                                color = Color(0xFF4D4D4D)
                            ),
                        ) {
                            Text(
                                text = "Продолжить без аккаунта",
                                color = textColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthPreview() {
    GoogleAuthScreen()
}