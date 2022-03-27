package com.kikoproject.uwidget.networking

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.SignInAccount
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kikoproject.uwidget.dialogs.ShowErrorDialog
import com.kikoproject.uwidget.dialogs.ShowLoadingDialog
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.navigation.ScreenNav

@Composable
fun CheckUserInDB(context: Context, state: MutableState<Boolean>, account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context), textError: String) {
    val db = Firebase.firestore
    if(account != null) {
        if (isOnline(context)) { // Проверка на онлайн
            ShowLoadingDialog(state)
            db.collection("users").document(account.id.toString()).get()
                .addOnCompleteListener {
                    val document = it.result
                    state.value = false
                    if (document.exists()) { // Уже все есть
                        navController.navigate(ScreenNav.ScheduleChooseNav.route) {
                            popUpTo(ScreenNav.ScheduleChooseNav.route)
                        }
                    } else {
                        navController.navigate(ScreenNav.RegistrationNav.route) // Если пользователь уже залогинен но не создал юзера
                    }
                }
        } else {
            ShowErrorDialog(textError)
        }
    }
    else{
        navController.navigate(ScreenNav.GoogleAuthNav.route) // Если нет то гуглимся
    }
}