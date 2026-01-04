package com.nochunsam.makeyourmorning.utilities.database

import androidx.room.TypeConverter
import org.json.JSONArray

class AppTypeConverter {
    @TypeConverter
    fun fromListIntToString(intList: List<Int>): String {
        return JSONArray(intList).toString()
    }
    @TypeConverter
    fun toListIntFromString(stringList: String): List<Int> {
        val jsonArray = JSONArray(stringList)
        val result = mutableListOf<Int>()
        for (i in 0 until jsonArray.length()) {
            result.add(jsonArray.getInt(i))
        }
        return result
    }
}