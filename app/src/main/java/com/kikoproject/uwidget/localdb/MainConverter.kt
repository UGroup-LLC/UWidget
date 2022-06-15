package com.kikoproject.uwidget.localdb

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.kikoproject.uwidget.models.schedules.options.ScheduleOptions
import java.io.ByteArrayOutputStream

/**
 * Конвертер для локальной БД
 *
 * @author Kiko
 */
class MainConverter {
    /**
     * Концвертация из base64 в картинку (Bitmap)
     * @author Kiko
     */
    @TypeConverter
    fun fromBase64ToBitmap(value: String?): Bitmap? {
        val imageBytes = Base64.decode(value, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    /**
     * Конвертация из картинки (Bitmap) в base64
     * @author Kiko
     */
    @TypeConverter
    fun fromBitmapToBase64(bitmap: Bitmap?): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    /**
     * Конвертация из gson строки в массив строк
     * @author Kiko
     */
    @TypeConverter
    fun restoreListOfString(listOfString: String?): List<String?>? {
        return Gson().fromJson(listOfString, object : TypeToken<List<String?>?>() {}.type)
    }

    /**
     * Конвертация из массива строк в gson строку
     * @author Kiko
     */
    @TypeConverter
    fun saveListOfString(listOfString: List<String?>?): String? {
        return Gson().toJson(listOfString)
    }

    /**
     * Конвертация из gson строки в расписание
     * @author Kiko
     */
    @TypeConverter
    fun restoreListOfSchedule(listOfString: String?): Map<String, MutableList<String>> {
        return Gson().fromJson(listOfString, object : TypeToken<Map<String, MutableList<String>>>() {}.type)
    }

    /**
     * Конвертация из расписания в gson строку
     * @author Kiko
     */
    @TypeConverter
    fun saveListOfSchedule(mapOfSchedule: Map<String, MutableList<String>>): String? {
        return Gson().toJson(mapOfSchedule)
    }

    /**
     * Конвертация из gson строки в настройки расписания
     * @author Kiko
     */
    @TypeConverter
    fun restoreListOfOptions(listOfString: String?): ScheduleOptions? {
        return Gson().fromJson(listOfString, object : TypeToken<ScheduleOptions>() {}.type)
    }

    /**
     * Конвертация из настроек расписания в gson строку
     * @author Kiko
     */
    @TypeConverter
    fun saveListOfOption(mapOfSchedule: ScheduleOptions?): String? {
        return Gson().toJson(mapOfSchedule)
    }


}