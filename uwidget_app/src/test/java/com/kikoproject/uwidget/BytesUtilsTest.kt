package com.kikoproject.uwidget

import com.kikoproject.uwidget.networking.generateRandomByteArray
import com.kikoproject.uwidget.networking.split
import org.junit.Test

class BytesUtilsTest {
    @Test
    fun convertListStringToRangeTime() {
        val generated = generateRandomByteArray(10000)
        val newReturn = generated.split(999)
        assert(newReturn.size >= 11)
    }

    @Test
    fun convertToHex() {
        val generated = generateRandomByteArray(6_000_000)
        val newReturn = generated.split(5000)
        newReturn.forEachIndexed { index, byteArrayItem ->
            println(((index*byteArrayItem.size).toFloat()/generated.size.toFloat())*100)
        }
        assert(newReturn.size >= 1000)
    }
}