package com.example.tg_persistenciadatos.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "viviendas",
    foreignKeys = [
        ForeignKey(entity = Propietario::class, parentColumns = ["id"], childColumns = ["propietarioId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Direccion::class, parentColumns = ["id"], childColumns = ["direccionId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class Vivienda(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val modelo: String,
    val precio: Int,
    val propietarioId: Int,
    val direccionId: Int
) {
    // GSON rellena esto desde la API, pero Room lo ignora al guardar en la tabla 'viviendas'
    @Ignore
    @SerializedName("caracteristicasId")
    var caracteristicasId: List<Int> = emptyList()
}