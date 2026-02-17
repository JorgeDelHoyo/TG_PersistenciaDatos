package com.example.tg_persistenciadatos.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "viviendas",
    foreignKeys = [
        ForeignKey(
            entity = Propietario::class,
            parentColumns = ["id"],
            childColumns = ["propietarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Vivienda(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val titulo: String,
    val precio: Double,
    val imagen: String,
    val propietarioId: Int // FK para relacionar con Propietario
)