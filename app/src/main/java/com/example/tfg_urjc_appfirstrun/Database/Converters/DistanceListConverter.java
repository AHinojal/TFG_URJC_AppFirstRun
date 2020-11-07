package com.example.tfg_urjc_appfirstrun.Database.Converters;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class DistanceListConverter {
    @TypeConverter
    public List<String> storedStringToList(String value) {
        List<String> distance = Arrays.asList(value.split("\\s*,\\s*"));
        return distance;
    }

    @TypeConverter
    public String listToStoredString(List<String> list) {
        String value = "";

        for (String lang : list)
            value += lang + ",";

        return value;
    }
}