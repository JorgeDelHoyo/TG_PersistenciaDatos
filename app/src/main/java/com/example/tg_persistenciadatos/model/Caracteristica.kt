package com.example.tg_persistenciadatos.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "caracteristicas")
data class Caracteristica(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val nombre: String
)

// TABLA DE CRUCE (N:M)
@Serializable
@Entity(
    tableName = "vivienda_caracteristica_cruce",
    primaryKeys = ["viviendaId", "caracteristicaId"],
    foreignKeys = [
        ForeignKey(
            entity = Vivienda::class,
            parentColumns = ["id"],
            childColumns = ["viviendaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Caracteristica::class,
            parentColumns = ["id"],
            childColumns = ["caracteristicaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ViviendaCaracteristicaCrossRef(
    val viviendaId: Int,
    val caracteristicaId: Int
)