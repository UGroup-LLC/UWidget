package com.kikoproject.uwidget.objects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.navController

@Composable
fun MainHeader(account: GoogleSignInAccount?) {
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top) {
        if (account != null) {
            Avatar(
                account = account, modifier = Modifier
                    .requiredSize(36.dp)
                    .clip(CircleShape)
                    .weight(2f)
            )
        }
        Text(
            text = "Главная",
            modifier = Modifier
                .weight(10f),
            color = MaterialTheme.colors.surface,
            style = MaterialTheme.typography.h6
        )
        IconButton(
            onClick = { /*TODO*/ }, modifier = Modifier
                .weight(2f)
                .requiredSize(36.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_moon),
                contentDescription = null,
                tint = Color(0xFFBF842C),
                modifier = Modifier.requiredSize(24.dp)
            )
        }
    } // header
}

@Composable
fun BackHeader(text: String) {
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top) {
        IconButton(
            onClick = { navController.popBackStack() }, modifier = Modifier
                .weight(2f)
                .requiredSize(36.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.requiredSize(24.dp)
            )
        }
        Text(
            text = text,
            modifier = Modifier
                .weight(10f),
            color = MaterialTheme.colors.surface,
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.requiredSize(24.dp))
    }
} // header