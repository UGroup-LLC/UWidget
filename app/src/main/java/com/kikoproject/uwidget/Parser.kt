package com.kikoproject.uwidget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.kikoproject.uwidget.models.SchedulesModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import kotlin.math.abs


fun getSelectorDivider(first: String, second: String): MutableList<Triple<Int, Int, Int>> {

    var beginPos = 0
    var endPos = 0
    var finishedFirstString = ""
    var finishedSecondString = ""

    val tempList = mutableListOf<Triple<Int, Int, Int>>()

    first.forEachIndexed { index, letter ->

        if (letter.toString().toIntOrNull() != null) {
            if (beginPos == 0) {
                beginPos = index
            }
            finishedFirstString += letter
            finishedSecondString += second[index]
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


fun getSchedule(
    scope: CoroutineScope,
    url: String,
    selector: String,
    lessonSelector: MutableList<Triple<Int, Int, Int>>,
    daySelector: MutableList<Triple<Int, Int, Int>>,
    lessonCount: Int,
    result: ScheduleGetter
) {
    val tempList = mutableListOf<Pair<Int, String>>()
    scope.launch(Dispatchers.IO) {
        Jsoup.connect(url).get().also {
            for (day: Int in 0..5) // Парсим дни
            {
                for (lesson: Int in 0..lessonCount) {
                    var fUrl = selector
                    lessonSelector.forEach { lessonSelector ->
                        fUrl = fUrl.replaceRange(
                            lessonSelector.first,
                            lessonSelector.second,
                            (fUrl.substring(lessonSelector.first, lessonSelector.second)
                                .toInt() + (lessonSelector.third * lesson)).toString()
                        )

                    }
                    daySelector.forEach { daySelector ->
                        fUrl = fUrl.replaceRange(
                            daySelector.first,
                            daySelector.second,
                            (fUrl.substring(daySelector.first, daySelector.second)
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

interface ScheduleGetter {
    fun onResult(schedules: MutableList<Pair<Int, String>>)
}
