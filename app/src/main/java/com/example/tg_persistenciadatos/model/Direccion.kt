package com.example.tg_persistenciadatos.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "direcciones")
data class Direccion(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val ciudad: String,
    val calle: String,
    @SerializedName("Piso") val piso: String
)