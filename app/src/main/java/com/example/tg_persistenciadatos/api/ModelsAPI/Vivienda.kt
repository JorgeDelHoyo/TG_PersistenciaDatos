package com.example.tg_persistenciadatos.api

import com.google.gson.annotations.SerializedName

data class Vivienda(
    val id: Int,
    val modelo: String,
    val precio: Int,
    val propietarioId: Int,
    val direccionId: Int,

    @SerializedName("caracteristicasId")
    val caracteristicaIds: List<Int>
)