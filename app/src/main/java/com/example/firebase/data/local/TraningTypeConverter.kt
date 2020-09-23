package com.example.firebase.data.local

import android.text.TextUtils
import androidx.room.TypeConverter
import com.example.firebase.data.model.LatlngPoints
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mapbox.geojson.Point

object TraningTypeConverter {

    //Object
    @JvmStatic
    @TypeConverter
    fun coordToString(model: LatlngPoints): String {
        return Gson().toJson(model)
    }

    @JvmStatic
    @TypeConverter
    fun coordToObject(text: String):  LatlngPoints? {
        if (TextUtils.isEmpty(text))
            return null
        return Gson().fromJson(text,  LatlngPoints::class.java)
    }

    // //array of object
    @JvmStatic
    @TypeConverter
    fun weatherToString(model: List<Point>): String {
        return Gson().toJson(model)
    }

    @JvmStatic
    @TypeConverter
    fun weatherToObject(text: String?): List<Point>? {
        if (text == null) return mutableListOf()
        val typeToken = object : TypeToken<List<Point>>() {}.type
        return Gson().fromJson(text, typeToken)
    }

}