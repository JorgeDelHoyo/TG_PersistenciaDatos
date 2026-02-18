package com.example.tg_persistenciadatos.api

import com.google.gson.annotations.SerializedName

data class Caracteristica(
    val id: Int,
    val nombre: String,

    val viviendasIds: List<Int>? = null
)