package com.example.tg_persistenciadatos.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "propietarios")
data class Propietario(
    @PrimaryKey(autoGenerate = false) // false porque el ID viene del JSON
    val id: Int,
    val nombre: String,
    val email: String
)