package com.example.tg_persistenciadatos.api

import com.google.gson.annotations.SerializedName

data class Direccion(
    val id: Int,
    val ciudad: String,
    val calle: String,

    // Mapea "Piso" (del JSON) a "piso" (variable Kotlin)
    @SerializedName("Piso")
    val piso: String
)