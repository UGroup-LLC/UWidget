package com.kikoproject.uwidget.networking

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kikoproject.uwidget.dialogs.ShowErrorDialog
import com.kikoproject.uwidget.dialogs.ShowLoadingDialog
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.models.SchedulesModel

@Composable
fun CheckUserInDB(
    context: Context,
    state: MutableState<Boolean>,
    account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context),
    textError: String
) {
    val db = Firebase.firestore
    if (account != null) {
        if (isOnline(context)) { // Проверка на онлайн
            ShowLoadingDialog(state)
            db.collection("users").document(account.id.toString()).get()
                .addOnCompleteListener {
                    val document = it.result
                    if (document.exists()) { // Уже все есть
                        state.value = false

                        navController.navigate(ScreenNav.ScheduleChooseNav.route) {
                            popUpTo(ScreenNav.ScheduleChooseNav.route)
                        }
                    } else {
                        state.value = false
                        navController.navigate(ScreenNav.RegistrationNav.route) // Если пользователь уже залогинен но не создал юзера
                    }
                }
        } else {
            ShowErrorDialog(textError)
        }
    } else {
        navController.navigate(ScreenNav.GoogleAuthNav.route) // Если нет то гуглимся
    }
}

fun getAllSchedules(scheduleResult: ScheduleResult) {
    val db = Firebase.firestore
    var schedulesModel = mutableListOf<SchedulesModel>()

    lateinit var documents: List<DocumentSnapshot>
    db.collection("schedules").get().addOnSuccessListener {
        documents = it.documents

        documents.forEach { doc ->
            schedulesModel.add(
                SchedulesModel(
                    doc.get("name").toString(),
                    doc.get("admin").toString(),
                    doc.get("users_ids") as List<String>,
                    doc.get("days") as List<Map<String, String>>,
                    doc.get("category").toString()
                )
            )
        }

        scheduleResult.onResult(schedulesModel)

    }.addOnFailureListener {
        scheduleResult.onError(it)
    }
}

interface ScheduleResult {
    fun onResult(schedules: List<SchedulesModel>)
    fun onError(error: Throwable)
}