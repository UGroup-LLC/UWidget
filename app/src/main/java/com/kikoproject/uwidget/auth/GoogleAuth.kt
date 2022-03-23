package com.kikoproject.uwidget.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.ui.theme.UWidgetTheme
import com.kikoproject.uwidget.ui.theme.themeTextColor

@Composable
fun GoogleAuthScreen() {
    val context = LocalContext.current
    val launchSign =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                navController.navigate(ScreenNav.RegistrationNav.route)
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }

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

                val textColor = themeTextColor()

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
                            color = MaterialTheme.colors.primary
                        )
                    ) {
                        append("Widget")
                    }
                }

                val linksTextAnnotation = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = textColor
                        )
                    ) {
                        append("Продолжая, вы соглашаетесь с ")
                    }

                    pushStringAnnotation(
                        tag = "terms",
                        annotation = "http://kikoproject.atwebpages.com/terms_of_use.html"
                    )
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("пользовательским соглашением ")
                    }
                    pop()

                    withStyle(
                        style = SpanStyle(
                            color = textColor
                        )
                    ) {
                        append("и с ")
                    }

                    pushStringAnnotation(
                        tag = "privacy",
                        annotation = "http://kikoproject.atwebpages.com/privacy_policy.html"
                    )

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("политикой конфидициальности")
                    }
                    pop()
                }


                Text(
                    text = colorTextAnnotation,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 20.dp, start = 30.dp),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = textColor
                )
                ClickableText(
                    text = linksTextAnnotation,
                    onClick = { offset ->
                        linksTextAnnotation.getStringAnnotations(
                            tag = "terms",
                            start = offset,
                            end = offset
                        ).firstOrNull()?.let {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                            context.startActivity(browserIntent)
                        }

                        linksTextAnnotation.getStringAnnotations(
                            tag = "privacy",
                            start = offset,
                            end = offset
                        ).firstOrNull()?.let {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                            context.startActivity(browserIntent)
                        }
                    },
                    modifier = Modifier.padding(top = 20.dp, start = 30.dp, end = 30.dp),
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
                            onClick = { googleSignIn(context, launchSign) },
                            border = BorderStroke(
                                1.dp,
                                color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.6f)
                            ),
                            colors = ButtonDefaults.buttonColors(
                                MaterialTheme.colors.primaryVariant.copy(
                                    alpha = 0.1f
                                )
                            )
                        ) {
                            Box(contentAlignment = Alignment.CenterStart) {
                                Image(
                                    colorFilter = ColorFilter.tint(
                                        color = MaterialTheme.colors.primaryVariant,
                                        blendMode = BlendMode.SrcAtop
                                    ),
                                    painter = painterResource(id = R.drawable.google),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(ButtonDefaults.IconSize + 18.dp)
                                        .padding(end = 10.dp),
                                )
                            }
                            Text(
                                text = "Войти с помощью Google",
                                color = MaterialTheme.colors.primaryVariant
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

fun googleSignIn( // Вход в гугл аккаунт
    context: Context,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken("768135781097-2r401ogli9pc1fmsg20hh0nfie5jp99m.apps.googleusercontent.com")
        .build()
    val googleSignInClient =
        com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(context, gso)
    val mAuth = FirebaseAuth.getInstance()
    signInOpen(googleSignInClient, context, launcher) // Запуск активити
}

fun signInOpen(
    googleSignInClient: GoogleSignInClient,
    context: Context,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    launcher.launch(googleSignInClient.signInIntent)
}

@Preview(showBackground = true)
@Composable
fun AuthPreview() {
    GoogleAuthScreen()
}