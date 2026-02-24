package com.example.tg_persistenciadatos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "propietarios")
data class Propietario(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val nombre: String
)