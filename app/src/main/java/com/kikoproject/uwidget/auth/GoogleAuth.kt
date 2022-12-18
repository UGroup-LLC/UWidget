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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.roomDb
import com.kikoproject.uwidget.networking.CheckUserInDB
import com.kikoproject.uwidget.ui.theme.UWidgetTheme

/**
 * Основное окно авторизации/регистрации через Google
 *
 * @author Kiko
 */
@Composable
fun GoogleAuthScreen() {
    val context = LocalContext.current

    // Получаем аккаунт пользователя, если он есть, если нет то null
    val account = remember { mutableStateOf(GoogleSignIn.getLastSignedInAccount(context)) }

    val stateLoading = remember { mutableStateOf(false) }

    if (stateLoading.value) { // Если нужен переход то вызываем Loading
        if (account.value != null) {
            LoadNextNav(context = context, state = stateLoading, account.value!!)
        }
        else{
            TODO("Выкинуть ошибку")
        }
    }

    // Отвечает за запуск входа в аккаунт Google
    val launchSign =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            account.value = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.value!!.idToken!!, null)

            try {
                // Показывает FireBase что есть вход из под аккаунта пользователя
                val auth = Firebase.auth
                auth.signInWithCredential(credential)

                stateLoading.value = true
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }

    UWidgetTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                val textColor = MaterialTheme.colors.surface

                // Весь текст где у нас правила использования приложения
                LinkTermsText(textColor, context)

                // Текст по середине где описано для чего будет использован аккаунт
                MainText(textColor)

                // Кнопки Входа и тд
                AllButtons(context, launchSign, account, textColor)
            }
        }
    }
}

/**
 * Кнопки которые будут использоваться в регистрации/авторизации
 *
 * @param launchSign переменная для вызова авторизации через Google и возращающая результат
 * @param account аккаунт гугл (может быть null)
 * @param textColor цвет текста
 *
 * @author Kiko
 */
@Composable
private fun AllButtons(
    context: Context,
    launchSign: ManagedActivityResultLauncher<Intent, ActivityResult>,
    account: MutableState<GoogleSignInAccount?>,
    textColor: Color
) {
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

            // Если получится так что уже зарегестрированный пользователь будет на этом экране он сможет выйти из аккаунта
            SignOutButton(account, context)

            /*OutlinedButton(
                modifier = Modifier.padding(top = 10.dp),
                onClick = {TODO("Сделать вход без аккаунта")},
                border = BorderStroke(
                    1.dp,
                    color = Color(0xFF4D4D4D)
                ),
            ) {
                Text(
                    text = "Продолжить без аккаунта",
                    color = textColor
                )
            }*/
        }
    }
}

/**
 * Основной текст в середине который поясняет зачем необходим аккаунт
 *
 * @param textColor цвет текста
 *
 * @author Kiko
 */
@Composable
private fun MainText(textColor: Color) {
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
}

/**
 * Текст хедера где пользователю отображается кликабельный текст политики конфидициальности и тд.
 *
 * @param textColor цвет текста
 *
 * @author Kiko
 */
@Composable
private fun LinkTermsText(
    textColor: Color,
    context: Context
) {
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
}

/**
 * Вход в аккаунт гугл
 *
 * @param launcher запускает авторизацию/регистрацию через Google и возвращает результат
 *
 * @author Kiko
 */
fun googleSignIn(
    context: Context,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken("768135781097-2r401ogli9pc1fmsg20hh0nfie5jp99m.apps.googleusercontent.com")
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    signInOpen(googleSignInClient, launcher) // Запуск активити
}

/**
 * Выход из аккаунта Google
 *
 * @author Kiko
 */
fun signOut(context: Context) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken("768135781097-2r401ogli9pc1fmsg20hh0nfie5jp99m.apps.googleusercontent.com")
        .build()
    val googleSignInClient =
        GoogleSignIn.getClient(context, gso)
    googleSignInClient.signOut()
}

/**
 * Запускает авторизацию/авторизацию через Google
 *
 * @param launcher запускает авторизацию/регистрацию через Google и возвращает результат
 *
 * @author Kiko
 */
fun signInOpen(
    googleSignInClient: GoogleSignInClient,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
) {
    launcher.launch(googleSignInClient.signInIntent)
}


/**
 * После авторизации выберает что необходимо делать далее, регистрировать пользователя или
 * пускать он уже и так зарегистрирован и пускать его в приложение
 *
 * @param state переменная отображающая диалог загрузки
 * @param account Google аккаунт
 *
 * @author Kiko
 */
@Composable
fun LoadNextNav(context: Context, state: MutableState<Boolean>, account: GoogleSignInAccount) {
    CheckUserInDB(
        context = context,
        state,
        account = account
    )
}

/**
 * Кнопка для выхода из аккаунта Google
 *
 * @param account Google аккаунт
 *
 * @author Kiko
 */
@Composable
fun SignOutButton(account: MutableState<GoogleSignInAccount?>, context: Context) { // Показывает кнопку выхода из аккаунта если юзер уже залогинен и находится в этом окне
    val textColor = MaterialTheme.colors.surface
    if (account.value != null) {
        OutlinedButton(
            modifier = Modifier.padding(top = 10.dp),
            onClick = {
                signOut(context = context) // Выходим из аккаунта
                account.value = null // Чтобы скрыть кнопку
                roomDb?.clearAllTables()
            },
            border = BorderStroke(
                1.dp,
                color = Color(0xFF4D4D4D)
            ),
        ) {
            Text(
                text = "Выйти из текущего аккаунта",
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthPreview() {
    GoogleAuthScreen()
}