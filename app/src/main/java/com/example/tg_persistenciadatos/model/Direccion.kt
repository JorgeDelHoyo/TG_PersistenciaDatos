package com.example.tg_persistenciadatos.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "direcciones",
    foreignKeys = [
        ForeignKey(
            entity = Vivienda::class,
            parentColumns = ["id"],
            childColumns = ["viviendaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Direccion(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val calle: String,
    val ciudad: String,
    val viviendaId: Int // FK para relacionar con Vivienda (1:1)
)