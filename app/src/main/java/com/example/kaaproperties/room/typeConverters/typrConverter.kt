package com.example.kaaproperties.room.typeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class typeConverter {
    @TypeConverter
    fun fromString(value: String?): ArrayList<String>{
        val listType = object :TypeToken<ArrayList<String>>(){}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArratList(List: ArrayList<String?>): String{
        return Gson().toJson(List)
    }
}