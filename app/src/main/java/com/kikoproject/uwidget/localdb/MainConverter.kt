package com.kikoproject.uwidget.localdb

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.kikoproject.uwidget.models.schedules.options.ScheduleOptions
import java.io.ByteArrayOutputStream


class MainConverter {
    @TypeConverter
    fun fromBase64ToBitmap(value: String?): Bitmap? {
        val imageBytes = Base64.decode(value, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    @TypeConverter
    fun fromBitmapToBase64(bitmap: Bitmap?): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @TypeConverter
    fun restoreListOfString(listOfString: String?): List<String?>? {
        return Gson().fromJson(listOfString, object : TypeToken<List<String?>?>() {}.type)
    }

    @TypeConverter
    fun saveListOfString(listOfString: List<String?>?): String? {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun restoreListOfSchedule(listOfString: String?): Map<String, MutableList<String>> {
        return Gson().fromJson(listOfString, object : TypeToken<Map<String, MutableList<String>>>() {}.type)
    }

    @TypeConverter
    fun saveListOfString(mapOfSchedule: Map<String, MutableList<String>>): String? {
        return Gson().toJson(mapOfSchedule)
    }

    @TypeConverter
    fun restoreListOfOptions(listOfString: String?): ScheduleOptions? {
        return Gson().fromJson(listOfString, object : TypeToken<ScheduleOptions>() {}.type)
    }

    @TypeConverter
    fun saveListOfOption(mapOfSchedule: ScheduleOptions?): String? {
        return Gson().toJson(mapOfSchedule)
    }


}