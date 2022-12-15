package com.kikoproject.uwidget

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
}