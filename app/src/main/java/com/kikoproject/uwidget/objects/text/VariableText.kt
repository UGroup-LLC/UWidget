package com.kikoproject.uwidget.objects.text

import com.kikoproject.uwidget.main.curUser
import com.kikoproject.uwidget.models.User

/**
 * @param user пользователь
 * @return лист всех возможных переменных в виде текстовых сокращений
 * @author Kiko
 */
private fun textVariables(user: User): List<Pair<String,String>> {
    return listOf(
        Pair("%n", user.Name),
    )
}
/**
 * @param user пользователь
 * @return текст преобразованный без сокращений, с переменными
 * @author Kiko
 */
fun String.variablize(user: User = curUser): String{
    var returnText = this
    textVariables(user).forEach { textVar ->
        returnText = returnText.replace(textVar.first,textVar.second)
    }
    return returnText
}