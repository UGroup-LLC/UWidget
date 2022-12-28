package com.kikoproject.uwidget.auth

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.curUser
import com.kikoproject.uwidget.main.db
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.ui.theme.UWidgetTheme
import com.kikoproject.uwidget.utils.bitmapCompress
import com.kikoproject.uwidget.utils.bitmapCrop
import com.kikoproject.uwidget.utils.bitmapResize
import java.io.ByteArrayOutputStream


/**
 * Экран регистрации
 * @author Kiko
 */
@Composable
fun RegisterScreen() {
    UWidgetTheme {
        val onSurfaceColor = MaterialTheme.colorScheme.onSurface
        val context = LocalContext.current
        val account = GoogleSignIn.getLastSignedInAccount(context)
        lateinit var customResizedBitmap: Bitmap

        var customImageUri by remember {
            mutableStateOf<Uri?>(null)
        }
        val customBitmap = remember { // Картинка если мы ее выбре
            mutableStateOf<Bitmap?>(null)
        }

        var nameState = remember { mutableStateOf(TextFieldValue(text = "")) }
        var surnameState = remember { mutableStateOf(TextFieldValue(text = "")) }

        // Активити который откроет выбор изображения из локальных фото
        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract =
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            // После получения фото передаем его в основную переменную
            customImageUri = uri
        }
        customImageUri?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder
                    .createSource(context.contentResolver, it)
                customBitmap.value = ImageDecoder.decodeBitmap(source)

                customBitmap.value?.let { btm ->
                    Image(
                        bitmap = btm.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.size(400.dp)
                    )
                }
            }
            else{
                Toast.makeText(context, "Доступно только с API 28+", Toast.LENGTH_SHORT).show()
            }
        }

        val showLoadingDialog = remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.padding(20.dp))
                Text(
                    text = "Введите данные профиля",
                    fontFamily = FontFamily(Font(R.font.gogh)),
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Divider(
                    color = onSurfaceColor.copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
                Spacer(modifier = Modifier.padding(10.dp))


                // Подгружаемая картинка из сети, фото аккаунта пользователя
                SubcomposeAsyncImage(
                    alignment = Alignment.Center,
                    model = account?.photoUrl,
                    loading = {
                        // При загрузке показываем индикатор загрузки
                        CircularProgressIndicator(
                            strokeWidth = 2.dp
                        )
                    },
                    success = {
                        // Если пользователь использует свою фотографию
                        if (customBitmap.value != null) {
                            customBitmap.value?.let { btm ->
                                // Обрезаем фото до квадрата
                                customResizedBitmap = bitmapCrop(btm)
                                // Уменьшаем
                                customResizedBitmap = bitmapResize(customResizedBitmap, 200, 200)
                                // Сжимаем
                                customResizedBitmap = bitmapCompress(customResizedBitmap, 90)
                                Image(
                                    bitmap = customResizedBitmap.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        } else {
                            SubcomposeAsyncImageContent()
                            customResizedBitmap = it.result.drawable.toBitmap()
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.primary)
                        ) {
                            if (account != null && account.givenName != null) {
                                Text(
                                    textAlign = TextAlign.Center,
                                    text = account.givenName!![0].toString(),
                                    color = Color.White,
                                    fontSize = 64.sp,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text(
                                    textAlign = TextAlign.Center,
                                    text = "L",
                                    color = Color.White,
                                    fontSize = 64.sp,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    },
                    contentDescription = "avatar",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") }
                )

                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    textAlign = TextAlign.Center,
                    text = "Отображаемый аватар\n(Нажмите для изменения)",
                    color = onSurfaceColor.copy(alpha = 0.4f)
                )
                Spacer(modifier = Modifier.padding(10.dp))

                val settedInput = false
                if (account != null && !settedInput) { // Если аккаунт есть то записываем в поля имя и фамилию аккаунта
                    if (account.givenName != null) {
                        nameState =
                            remember { mutableStateOf(TextFieldValue(text = account.givenName!!)) }
                    }
                    if (account.familyName != null) {
                        surnameState =
                            remember { mutableStateOf(TextFieldValue(text = account.familyName!!)) }
                    }
                }

                // Поля для ввода
                TextsInput(nameState, surnameState)

                Spacer(modifier = Modifier.padding(4.dp))

                Button(
                    onClick = {
                        showLoadingDialog.value = true

                        if (account != null) {
                            sendToDBMainInfo(
                                nameState.value.text,
                                surnameState.value.text,
                                bitmapToBase64(customResizedBitmap),
                                account
                            )
                        }
                        if (account != null && account.id != null) {
                            curUser = User(
                                nameState.value.text,
                                surnameState.value.text,
                                customResizedBitmap,
                                account.id!!
                            )
                        }
                        navController.navigate(ScreenNav.Dashboard.route)
                    }, colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text(text = "Подтвердить выбор", color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

/**
 * Поля для ввода имени и фамилии юзера
 *
 * @param nameState Введенное имя пользователя
 * @param surnameState Введенная фамилия пользователя
 *
 * @author Kiko
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextsInput(
    nameState: MutableState<TextFieldValue>,
    surnameState: MutableState<TextFieldValue>
) {
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    OutlinedTextField(
        value = nameState.value,
        onValueChange = { nameState.value = it },
        label = {
            Text(
                text = "Введите отображаемое имя",
                color = onSurfaceColor.copy(alpha = 0.4f)
            )
        },
        shape = RoundedCornerShape(16.dp)
    )
    Spacer(modifier = Modifier.padding(4.dp))
    OutlinedTextField(
        value = surnameState.value,
        onValueChange = { surnameState.value = it },
        label = {
            Text(
                text = "Введите отображаемую фамилию",
                color = onSurfaceColor.copy(alpha = 0.4f)
            )
        },
        shape = RoundedCornerShape(16.dp)
    )
}

/**
 * Отправка информации о новом аккаунте в БД
 *
 * @param name имя аккаунта
 * @param surname фамилия аккаунта
 * @param bitmapBase64 аватар аккаунта в формате base64
 * @param account Google аккаунт
 *
 * @author Kiko
 */
fun sendToDBMainInfo(
    name: String,
    surname: String,
    bitmapBase64: String,
    account: GoogleSignInAccount
) {
    val user = hashMapOf(
        "name" to name,
        "surname" to surname,
        "image" to bitmapBase64,
        "id" to account.id
    )
    db.collection("users").document(account.id.toString()).set(user)
        .addOnSuccessListener {
            Log.d(TAG, "DocumentSnapshot setted")
            navController.navigate(ScreenNav.Dashboard.route)
        }
        .addOnFailureListener { e ->
            Log.w(TAG, "Error adding document", e)
        }
}

/**
 * Конвертирует картинку в base64 формат
 *
 * @param dstBmp исходная картинка
 *
 * @author Kiko
 */
fun bitmapToBase64(dstBmp: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    dstBmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}


@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterScreen()
}
