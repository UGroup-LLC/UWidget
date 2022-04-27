package com.kikoproject.uwidget

import android.content.Context
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.math.abs


fun getSelectorDivider(firstString: String, secondString: String): Triple<List<Triple<Int, Int, Int>>, String, List<Int>> {
    val firstList = mutableListOf<Int>()
    val secondList = mutableListOf<Int>()

    val returnList = mutableListOf<Triple<Int, Int, Int>>()

    var returnAll = Triple(listOf<Triple<Int, Int, Int>>(), String(), listOf<Int>())

    var tempStringDigital = ""
    var tempStringForNotDigital = ""
    val indexedPos = mutableListOf<Int>()


    /* Вытаскиваем все числа в массив и затем проверим одинакова ли длинна масивов */
    firstString.forEachIndexed { index, it ->
        if(it.isDigit()){
            tempStringDigital += it
        }
        else if(tempStringDigital.isNotEmpty()){
            firstList.add(tempStringDigital.toInt())
            indexedPos.add(index)
            tempStringDigital = ""
        }

        if(!it.isDigit()){
            tempStringForNotDigital += it
        }
    }

    secondString.forEach {
        if(it.isDigit()){
            tempStringDigital += it
        }
        else if(tempStringDigital.isNotEmpty()){
            secondList.add(tempStringDigital.toInt())
            tempStringDigital = ""
        }
    }

    if(secondList.size == firstList.size){
        secondList.forEach{ second ->
            firstList.forEach{first ->
                returnList.add(Triple(first, second, abs(first-second)))
            }
        }
    }

    returnAll = Triple(returnList, tempStringForNotDigital, indexedPos)

    return returnAll
}

fun findSelectors(
    scope: CoroutineScope,
    url: String,
    textFind: String,
    result: ScheduleGetterSelectors
) {
    val tempList = Pair(mutableListOf<String>(), mutableListOf<String>())
    val webClient = WebClient()
    webClient.options.isJavaScriptEnabled = false

    scope.launch(Dispatchers.IO) {
        val page = webClient.getPage<HtmlPage>(url)
        val doc = Jsoup.parse(page.webResponse.contentAsString)
            doc.getElementsContainingOwnText(textFind).forEach {
                val text = (it.parent()
                    ?.text() ?: "")
                if (!tempList.first.contains(text)) {
                    tempList.first.add(text)
                    tempList.second.add(it.cssSelector())
                }
            }

            result.onResult(tempList)
        }
}

fun getSchedule( // Получение расписания
    scope: CoroutineScope,
    url: String,
    selector: String,
    lessonSelector: List<Triple<Int, Int, Int>>, // Селектор для пары
    daySelector: List<Triple<Int, Int, Int>>, // Селектор для дня
    lessonCount: Int,
    result: ScheduleGetter
) {
    //var calibration = mutableStateOf("")

    val tempList = mutableListOf<Pair<Int, String>>()
    scope.launch(Dispatchers.IO) {
        Jsoup.connect(url).get().also { it ->
            val itteration = 10

           // calibration.value = getCalibratedSelector(itteration, selector, lessonSelector, it, calibration)

            for (day: Int in 0..5) // Парсим дни
            {
                for (lesson: Int in 0..lessonCount) {
                    var fUrl = selector
                   /* if(calibration.value != selector) {
                        fUrl = calibration.value
                    }*/
                    lessonSelector.forEach { lessonSelector ->
                        val realSecLength = fUrl.substring(lessonSelector.first)
                            .filter { it.isDigit() }.length

                        fUrl = fUrl.replaceRange(
                            lessonSelector.first,
                            lessonSelector.first + realSecLength,
                            (fUrl.substring(lessonSelector.first, lessonSelector.second)
                                .filter { it.isDigit() }
                                .toInt() + (lessonSelector.third * lesson)).toString()
                        )
                    }
                    daySelector.forEach { daySelector ->
                        fUrl = fUrl.replaceRange(
                            daySelector.first,
                            daySelector.second,
                            (fUrl.substring(daySelector.first, daySelector.second)
                                .filter { it.isDigit() }
                                .toInt() + (daySelector.third + (day - daySelector.third))).toString() // Переводим отличие в INT и прибавляем к полученному числу день
                        )
                    }

                    tempList.add(Pair(day, it.select(fUrl).text()))
                }
            }
            result.onResult(tempList)
        }
    }
}
/*
private fun getCalibratedSelector(
    itteration: Int,
    selector: String,
    lessonSelector: MutableList<Triple<Int, Int, Int>>,
    it: Document,
    calibration: MutableState<String>
) : String  {
    for (lessonIndex: Int in 0..itteration) {
        var fUrl = selector
        var value = 0
        lessonSelector.forEach { lessonSelector ->
            val realSecLength = fUrl.substring(lessonSelector.first, lessonSelector.second)
                .filter { it.isDigit() }.length

            value = fUrl.substring(lessonSelector.first, lessonSelector.second)
                .filter { it.isDigit() }
                .toInt() - (lessonSelector.third * lessonIndex)

            fUrl = fUrl.replaceRange(
                lessonSelector.first,
                lessonSelector.first + realSecLength,
                value.toString()
            )
        }
        if (value < 0) break;

        if (it.select(fUrl).size > 0) {
            calibration.value = it.select(fUrl).text()
        }
    }
    return calibration.value
}*/

interface ScheduleGetter {
    fun onResult(schedules: MutableList<Pair<Int, String>>)
}

interface ScheduleGetterSelectors {
    fun onResult(schedules: Pair<MutableList<String>, MutableList<String>>)
}
