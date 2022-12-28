package com.kikoproject.uwidget.utils

/**
 * Чистит лист от елементов где нет текста
 *
 * @author Kiko
 */
fun List<String>.deleteWhitespaces() : List<String>{
    val returnArray = mutableListOf<String>()
    this.forEach{ s ->
        if(s.filter { !it.isWhitespace() } != "") {
            returnArray.add(s)
        }
    }
    return returnArray
}