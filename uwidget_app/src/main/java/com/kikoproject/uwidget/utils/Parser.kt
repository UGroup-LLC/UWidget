package com.kikoproject.uwidget.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import kotlin.math.abs

/**
 * TODO("ПЕРЕПИСАТЬ ЭТО ВСЕ ПОД ЧТО ТО БОЛЕЕ МЕНЕЕ")
 */
fun getSelectorDivider(first: String, second: String): MutableList<Triple<Int, Int, Int>> {

    var beginPos = 0
    var endPos = 0
    var finishedFirstString = ""
    var finishedSecondString = ""

    val tempList = mutableListOf<Triple<Int, Int, Int>>()

    second.forEachIndexed { index, letter ->

        if (letter.toString().toIntOrNull() != null) {
            if (beginPos == 0) {
                beginPos = index
            }
            if (first[index].toString().toIntOrNull() != null) {
                finishedFirstString += first[index]
            }
            finishedSecondString += letter
        } else {
            if (finishedFirstString != "" && finishedFirstString != finishedSecondString) {
                endPos = index
                val diff = finishedFirstString.toInt() - finishedSecondString.toInt()
                tempList.add(Triple(beginPos, endPos, abs(diff)))

                finishedFirstString = ""
                beginPos = 0
                finishedSecondString = ""
            } else {
                finishedFirstString = ""
                beginPos = 0
                finishedSecondString = ""
            }
        }

    }

    return tempList
}

fun getSelectors(
    scope: CoroutineScope,
    url: String,
    textFind: String,
    result: ScheduleGetterSelectors
) {
    val tempList = Pair(mutableListOf<String>(), mutableListOf<String>())
    scope.launch(Dispatchers.IO) {
        Jsoup.connect(url).get().also { doc ->
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

}

fun getSchedule( // Получение расписания
    scope: CoroutineScope,
    url: String,
    selector: String,
    lessonSelector: MutableList<Triple<Int, Int, Int>>, // Селектор для пары
    daySelector: MutableList<Triple<Int, Int, Int>>, // Селектор для дня
    lessonCount: Int,
    result: ScheduleGetter
) {
    val tempFinish = mutableListOf<MutableList<String>>()

    scope.launch(Dispatchers.IO) {
        Jsoup.connect(url).get().also { it ->
            val itteration = 10

           // calibration.value = getCalibratedSelector(itteration, selector, lessonSelector, it, calibration)

            for (day: Int in 0..5) // Парсим дни
            {
                val tempList = mutableListOf<String>()

                for (lesson: Int in 0 until lessonCount) {
                    var fUrl = selector
                    lessonSelector.forEach { lessonSelector ->
                        val realSecLength = fUrl.substring(lessonSelector.first)
                            .filter { it.isDigit() }.length

                        fUrl = fUrl.replaceRange(
                            lessonSelector.first,
                            lessonSelector.second,
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

                    tempList.add(it.select(fUrl).text())
                }
                tempFinish.add(tempList)
            }
            result.onResult(tempFinish)
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
    fun onResult(schedules: MutableList<MutableList<String>>)
}

interface ScheduleGetterSelectors {
    fun onResult(schedules: Pair<MutableList<String>, MutableList<String>>)
}
