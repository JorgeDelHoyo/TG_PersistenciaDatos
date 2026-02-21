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
    // Campo que lee la lista del JSON
    val caracteristicasId: List<Int> = emptyList()
)