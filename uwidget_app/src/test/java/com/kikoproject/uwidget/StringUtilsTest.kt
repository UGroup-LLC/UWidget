package com.kikoproject.uwidget

import com.kikoproject.uwidget.utils.distinctLesions
import com.kikoproject.uwidget.utils.distinctTime
import com.kikoproject.uwidget.utils.toTimeRange
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalTime

class StringUtilsTest {
    @Test
    fun convertListStringToRangeTime() {
        val inputString = listOf("12:32..12:43", "14:12..15:23")

        val firstInputTime = LocalTime.of(12, 32)..LocalTime.of(12, 43)
        val secondInputTime = LocalTime.of(14, 12)..LocalTime.of(15, 23)

        val returnedArray = listOf(firstInputTime, secondInputTime)

        assertEquals(returnedArray, inputString.toTimeRange())
    }

    @Test
    fun convertListStringToRangeTimeWithNull() {
        for (i: Int in 0..50) {
            val inputString = listOf(
                "${(0..444444).random()}..${(0..444444).random()}",
                "${(0..444444).random()}..${(0..444444).random()}"
            )

            assertEquals(null, inputString.toTimeRange())
        }

        for (i: Int in 0..50) {
            val inputString = listOf(
                "${("asb".."asv")}..${("asb".."asv")}",
                "${("sdfsf2".."gfhfh")}..${("qwe".."dfg3")}"
            )

            assertEquals(null, inputString.toTimeRange())
        }
    }

    @Test
    fun districtTimeFromSchedule() {
        val inputString = listOf(
            "09:00..09:45", "09:50..10:40",
            "11:20..11:45", "11:45..12:20",
            "12:45..13:10",
            "13:20..13:45",
            "14:00..14:45", "15:20..15:50")

        val schedule = listOf("Mat", "Mat", "Ol", "Ol", "Test", "Kok", "Lol", "Lol")

        val firstInputTime = LocalTime.of(9, 0)..LocalTime.of(10, 40)
        val secondInputTime = LocalTime.of(11, 20)..LocalTime.of(12, 20)
        val thirdInputTime = LocalTime.of(12, 45)..LocalTime.of(13, 10)
        val fourInputTime = LocalTime.of(13, 20)..LocalTime.of(13, 45)
        val fiveInputTime = LocalTime.of(14, 0)..LocalTime.of(15, 50)

        val returnedArray = listOf(
            firstInputTime,
            secondInputTime,
            thirdInputTime,
            fourInputTime,
            fiveInputTime)

        assertEquals(returnedArray, inputString.distinctTime(schedule))
    }

    @Test
    fun districtLesionFromSchedule() {
        val schedule = listOf("Mat", "Mat", "Ol", "Ol", "Test", "Kok", "Lol", "Lol", "Hi")

        val returnedArray = listOf(
            "Mat","Ol","Test","Kok","Lol", "Hi")

        assertEquals(returnedArray, schedule.distinctLesions())
    }
}