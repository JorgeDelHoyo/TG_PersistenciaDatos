package com.example.tg_persistenciadatos.data.local

import androidx.room.TypeConverter
/**
 * Room solo sabe guardar tipos primitivos (Int, String, etc.).
 * Esta clase traduce la lista de enteros (caracteristicasId) a un texto
 * separado por comas para poder guardarlo en SQLite, y viceversa.
 */
class Converters {
    @TypeConverter
    fun fromList(list: List<Int>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toList(data: String?): List<Int> {
        if (data.isNullOrEmpty()) return emptyList()
        return data.split(",").mapNotNull { it.toIntOrNull() }
    }
}