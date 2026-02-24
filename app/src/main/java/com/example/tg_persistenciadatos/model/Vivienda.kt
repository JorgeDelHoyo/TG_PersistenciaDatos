package com.example.tg_persistenciadatos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "viviendas")
data class Vivienda(
    @PrimaryKey
    val id: Int,
    val modelo: String,
    val precio: Int,
    val propietarioId: Int,
    val direccionId: Int,

    // Lista de IDs que viene del JSON de la API.
    // Room necesita la clase Converters para saber cómo guardar esta lista.
    val caracteristicasId: List<Int> = emptyList()
)