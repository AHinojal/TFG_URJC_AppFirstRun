package com.example.tfg_urjc_appfirstrun.Database.Converters

import androidx.room.TypeConverter
import java.util.*

class DistanceListConverter {
    @TypeConverter
    fun storedStringToList(value: String?): MutableList<String?>? {
        return Arrays.asList(*value!!.split("\\s*,\\s*")?.toTypedArray())
    }

    @TypeConverter
    fun listToStoredString(list: MutableList<String?>?): String? {
        var value = ""
        if (list != null) {
            for (lang in list) value += "$lang,"
        }
        return value
    }
}