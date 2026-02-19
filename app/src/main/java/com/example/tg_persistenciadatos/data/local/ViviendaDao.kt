package com.example.tg_persistenciadatos.data.local

import androidx.room.*
import com.example.tg_persistenciadatos.model.*

@Dao
interface ViviendaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropietarios(propietarios: List<Propietario>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirecciones(direcciones: List<Direccion>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertViviendas(viviendas: List<Vivienda>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCruce(cruce: List<ViviendaCaracteristicaCrossRef>)

    @Query("SELECT * FROM viviendas")
    suspend fun getAllViviendas(): List<Vivienda>

    @Query("SELECT * FROM propietarios")
    suspend fun getAllPropietarios(): List<Propietario>

    @Query("DELETE FROM viviendas")
    suspend fun deleteAllViviendas()

    @Query("DELETE FROM propietarios")
    suspend fun deleteAllPropietarios()

    @Query("DELETE FROM direcciones")
    suspend fun deleteAllDirecciones()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVivienda(vivienda: Vivienda)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDireccion(direccion: Direccion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaracteristicas(caracteristicas: List<Caracteristica>)
    @Update
    suspend fun updateVivienda(vivienda: Vivienda)

    @Delete
    suspend fun deleteVivienda(vivienda: Vivienda)
}