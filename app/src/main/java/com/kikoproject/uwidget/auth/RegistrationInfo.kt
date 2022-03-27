package com.kikoproject.uwidget.auth

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
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
import androidx.core.graphics.drawable.toDrawable
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.SubcomposeAsyncImageScope
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.ui.theme.UWidgetTheme
import com.kikoproject.uwidget.ui.theme.themeTextColor
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


@Composable
fun RegisterScreen() {
    UWidgetTheme {
        val textColor = themeTextColor()
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

        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract =
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            customImageUri = uri
        }
        customImageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                customBitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)

            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, it)
                customBitmap.value = ImageDecoder.decodeBitmap(source)
            }

            customBitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(400.dp)
                )
            }
        }

        val showLoadingDialog = remember{mutableStateOf(false)}

        //if(showLoadingDialog.value){
          //  ShowLoadingDialog()
        //}

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.padding(20.dp))
                Text(
                    text = "Введите данные профиля",
                    fontFamily = FontFamily(Font(R.font.gogh)),
                    color = textColor,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Divider(
                    color = textColor.copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
                Spacer(modifier = Modifier.padding(10.dp))

                SubcomposeAsyncImage(
                    alignment = Alignment.Center,
                    model = account?.photoUrl,
                    loading = {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colors.primary
                        )
                    },
                    success = {
                        if (customBitmap.value != null) {
                            customBitmap.value?.let { btm ->
                                customResizedBitmap = bitmapCrop(btm)
                                customResizedBitmap = bitmapResize(customResizedBitmap, 200, 200)
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
                                .background(color = MaterialTheme.colors.primary)// clip to the circle shape
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
                        .clip(CircleShape)                       // clip to the circle shape
                        .border(1.dp, Color.Gray, CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") }
                )

                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    textAlign = TextAlign.Center,
                    text = "Отображаемый аватар\n(Нажмите для изменения)",
                    color = textColor.copy(alpha = 0.4f)
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
                OutlinedTextField(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    label = {
                        Text(
                            text = "Введите отображаемое имя",
                            color = textColor.copy(alpha = 0.4f)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(color = textColor)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                OutlinedTextField(
                    value = surnameState.value,
                    onValueChange = { surnameState.value = it },
                    label = {
                        Text(
                            text = "Введите отображаемую фамилию",
                            color = textColor.copy(alpha = 0.4f)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(color = textColor)
                )

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
                    }, colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colors.primaryVariant
                    ),
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text(text = "Подтвердить выбор", color = MaterialTheme.colors.secondary)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterScreen()
}

fun bitmapCrop(
    srcBmp: Bitmap,
    widthCompress: Int = 1,
    heightCompress: Int = 1
): Bitmap { // Обрезает картику до квадрата
    val dstBmp: Bitmap
    if (srcBmp.getWidth() >= srcBmp.getHeight()) {

        dstBmp = Bitmap.createBitmap(
            srcBmp,
            srcBmp.getWidth() / 2 - srcBmp.getHeight() / 2,
            0,
            srcBmp.getHeight() / heightCompress,
            srcBmp.getHeight() / heightCompress
        );

    } else {

        dstBmp = Bitmap.createBitmap(
            srcBmp,
            0,
            srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2,
            srcBmp.getWidth() / widthCompress,
            srcBmp.getWidth() / widthCompress
        );
    }
    return dstBmp
}

fun bitmapResize(dstBmp: Bitmap, height: Int = 200, width: Int = 200): Bitmap {
    return Bitmap.createScaledBitmap(dstBmp, width, height, false)
}

fun bitmapCompress(dstBmp: Bitmap, quality: Int = 30): Bitmap {
    val out = ByteArrayOutputStream()
    dstBmp.compress(Bitmap.CompressFormat.JPEG, quality, out)
    return BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()));
}

fun sendToDBMainInfo(
    name: String,
    surname: String,
    bitmapBase64: String,
    account: GoogleSignInAccount
) {
    val db = Firebase.firestore
    val user = hashMapOf(
        "name" to name,
        "surname" to surname,
        "image" to bitmapBase64
    )
    db.collection("users").document(account.id.toString()).set(user)
        .addOnSuccessListener { documentReference ->
            Log.d(TAG, "DocumentSnapshot setted")
            navController.navigate(ScreenNav.ScheduleChooseNav.route)
        }
        .addOnFailureListener { e ->
            Log.w(TAG, "Error adding document", e)
        }
}

fun bitmapToBase64(dstBmp: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    dstBmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT);
}