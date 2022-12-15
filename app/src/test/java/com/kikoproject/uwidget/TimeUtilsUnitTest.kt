package com.kikoproject.uwidget

import com.kikoproject.uwidget.utils.getCloseTimeBetweenRange
import com.kikoproject.uwidget.utils.getCloseTimeRange
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalTime

class TimeUtilsUnitTest {
    @Test
    fun closeTimeRangeBetweenUnitTest(){
        val arrayTimes = listOf(fastTimes(11,12),fastTimes(14,15),fastTimes(16,17))

        assertEquals(fastTimes(12,14), LocalTime.of(13, 0).getCloseTimeBetweenRange(arrayTimes))
        assertEquals(fastTimes(15,16), LocalTime.of(15, 3).getCloseTimeBetweenRange(arrayTimes))

        assertEquals(null, LocalTime.of(18, 0).getCloseTimeBetweenRange(arrayTimes))
    }

    @Test
    fun closeTimeRangeUnitTest(){
        val arrayTimes = listOf(fastTimes(11,12),fastTimes(14,15),fastTimes(16,17))

        assertEquals(fastTimes(11,12), LocalTime.of(11, 2).getCloseTimeRange(arrayTimes))
    }

    private fun fastTimes(hours: Int, hours2: Int) : ClosedRange<LocalTime>{
        return LocalTime.of(hours, 0)..LocalTime.of(hours2, 0)
    }
}