package com.example.tg_persistenciadatos.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "vivienda_caracteristica_cruce",
    primaryKeys = ["viviendaId", "caracteristicaId"],
    foreignKeys = [
        ForeignKey(entity = Vivienda::class, parentColumns = ["id"], childColumns = ["viviendaId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Caracteristica::class, parentColumns = ["id"], childColumns = ["caracteristicaId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class ViviendaCaracteristicaCrossRef(
    val viviendaId: Int,
    val caracteristicaId: Int
)