package com.kikoproject.uwidget.networking

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import com.kikoproject.uwidget.dialogs.ShowErrorDialog
import com.kikoproject.uwidget.dialogs.ShowLoadingDialog
import com.kikoproject.uwidget.main.db
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.models.SchedulesModel
import com.kikoproject.uwidget.models.User

@Composable
fun CheckUserInDB(
    context: Context,
    state: MutableState<Boolean>,
    account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context),
    textError: String
) {
    if (account != null) {
        if (isOnline(context)) { // Проверка на онлайн
            ShowLoadingDialog(state)
            db.collection("users").document(account.id.toString()).get()
                .addOnCompleteListener {
                    val document = it.result
                    if (document.exists()) { // Уже все есть
                        state.value = false

                        getAllSchedules(object : ScheduleResult{
                            override fun onResult(schedules: List<SchedulesModel>) {
                                schedules.forEach{ schedule ->
                                    if(schedule.AdminID == account.id){
                                        navController.navigate(ScreenNav.Dashboard.route)
                                    }
                                    else{
                                        navController.navigate(ScreenNav.ScheduleChooseNav.route)
                                    }
                                }
                            }

                            override fun onError(error: Throwable) {
                                navController.navigate(ScreenNav.ScheduleChooseNav.route)
                            }
                        })
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

fun createScheduleInDB(schedule: SchedulesModel){
    if (schedule.Name != "") {
        val user = hashMapOf(
            "admin_id" to schedule.AdminID,
            "users_ids" to schedule.UsersID,
            "category" to schedule.Category,
            "name" to schedule.Name,
            "schedule" to schedule.Schedule,
            "time" to schedule.Time
        )
        db.collection("schedules").document().set(user)
    }
}

fun getAllSchedules(scheduleResult: ScheduleResult) {

    var schedulesModel = mutableListOf<SchedulesModel>()

    lateinit var documents: List<DocumentSnapshot>
    db.collection("schedules").get().addOnSuccessListener {
        documents = it.documents

        documents.forEach { doc ->
            schedulesModel.add(
                SchedulesModel(
                    doc.get("name").toString(),
                    doc.get("admin_id").toString(),
                    doc.get("users_ids") as List<String>,
                    doc.get("schedule") as Map<String, MutableList<String>>,
                    doc.get("time") as List<String>,
                    doc.get("category").toString()
                )
            )
        }

        scheduleResult.onResult(schedulesModel)

    }.addOnFailureListener {
        scheduleResult.onError(it)
    }
}

fun getUserFromDB(account: GoogleSignInAccount) : User? {
    var user: User? = null
    db.collection("users").get().addOnSuccessListener {
        it.documents.forEach { doc ->
            if (account.id == doc.get("id").toString()){
                val imageBytes = Base64.decode(doc.get("image").toString(), Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                val tempUser = User(doc.get("name").toString(), doc.get("surname").toString(), decodedImage, doc.get("id").toString())
                user = tempUser
            }
        }
    }
    return user
}

interface ScheduleResult {
    fun onResult(schedules: List<SchedulesModel>)
    fun onError(error: Throwable)
}