package com.kikoproject.uwidget.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.ui.theme.UWidgetTheme
import com.kikoproject.uwidget.ui.theme.themeTextColor

@Composable
fun RegisterScreen() {
    UWidgetTheme() {
        val textColor = themeTextColor()
        val context = LocalContext.current
        val account = GoogleSignIn.getLastSignedInAccount(context)
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
                    model = account?.photoUrl,
                    loading = {
                        CircularProgressIndicator(strokeWidth = 2.dp)
                    },
                    contentDescription = "avatar",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)                       // clip to the circle shape
                        .border(1.dp, Color.Gray, CircleShape)
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Отображаемый аватар",
                    color = textColor.copy(alpha = 0.4f)
                )
                Spacer(modifier = Modifier.padding(10.dp))

                val nameState = remember { mutableStateOf(TextFieldValue(text = "")) }
                val surnameState = remember { mutableStateOf(TextFieldValue(text = "")) }

                if (account != null) {
                    if (account.givenName != null) {
                        nameState.value = TextFieldValue(text = account.givenName!!)
                    }
                    if(account.familyName != null){
                        surnameState.value = TextFieldValue(text = account.familyName!!)
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
                    onClick = {}, colors = ButtonDefaults.buttonColors(
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