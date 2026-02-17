package com.example.tg_persistenciadatos.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.tg_persistenciadatos.model.Caracteristica
import com.example.tg_persistenciadatos.model.Direccion
import com.example.tg_persistenciadatos.model.Propietario
import com.example.tg_persistenciadatos.model.Vivienda
import com.example.tg_persistenciadatos.model.ViviendaCaracteristicaCrossRef

@Dao
interface ViviendaDao {

    // --- INSERTAR (Para guardar lo que viene de la API) ---
    // Usamos REPLACE para que si el ID ya existe, lo actualice (Update)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropietarios(propietarios: List<Propietario>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertViviendas(viviendas: List<Vivienda>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirecciones(direcciones: List<Direccion>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaracteristicas(caracteristicas: List<Caracteristica>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCruce(cruce: List<ViviendaCaracteristicaCrossRef>)

    // --- LEER (Consultas básicas) ---

    @Query("SELECT * FROM viviendas")
    suspend fun getAllViviendas(): List<Vivienda>

    @Query("SELECT * FROM propietarios")
    suspend fun getAllPropietarios(): List<Propietario>

    // --- BORRAR TODO (Para limpiar antes de recargar de la API) ---
    @Query("DELETE FROM viviendas")
    suspend fun deleteAll()

    // OPERACIONES CRUD INDIVIDUALES
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vivienda: Vivienda)

    @Update
    suspend fun update(vivienda: Vivienda)

    @Delete
    suspend fun delete(vivienda: Vivienda)
}