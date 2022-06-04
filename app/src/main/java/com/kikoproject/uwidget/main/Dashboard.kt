package com.kikoproject.uwidget.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.objects.Avatar
import com.kikoproject.uwidget.objects.MainHeader
import com.kikoproject.uwidget.objects.coloredText

@Composable
fun DashboardActivity() {
    val context = LocalContext.current
    val account = GoogleSignIn.getLastSignedInAccount(context)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 30.dp, vertical = 10.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MainHeader(account = account)
            Card( // content
                shape = RoundedCornerShape(20.dp),
                contentColor = MaterialTheme.colors.background,
                backgroundColor = MaterialTheme.colors.background,
                border = BorderStroke(1.dp, MaterialTheme.colors.primary)
            ) {
                Card(
                    shape = RoundedCornerShape(45.dp),
                    backgroundColor = MaterialTheme.colors.primary.copy(0.25f),
                    modifier = Modifier.padding(horizontal = 45.dp, vertical = 20.dp)
                ) {
                    if (account != null && account.givenName != null) {
                        Text(
                            text = coloredText(
                                first = "Доброе утро, ",
                                second = curUser.Name
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.surface
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(50.dp),
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp)
            ) {
                FloatingActionButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.requiredSize(65.dp),
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(19.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pencil),
                        modifier = Modifier.padding(21.dp),
                        contentDescription = null,
                        tint = Color.Black.copy(0.4f),
                    )
                }
                FloatingActionButton(
                    onClick = { navController.navigate(ScreenNav.AllSchedulesNav.route) },
                    modifier = Modifier.requiredSize(65.dp),
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(19.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_schedules),
                        modifier = Modifier.padding(21.dp),
                        contentDescription = null,
                        tint = Color.Black.copy(0.4f),
                    )
                }
                FloatingActionButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.requiredSize(65.dp),
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(19.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        modifier = Modifier.padding(20.dp),
                        contentDescription = null,
                        tint = Color.Black.copy(0.4f),
                    )
                }
            }
        }
    }
}

@Composable
fun ScheduleTexts() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

    }
}