package com.example.tg_persistenciadatos.data.local

import androidx.room.*
import com.example.tg_persistenciadatos.model.Caracteristica
import com.example.tg_persistenciadatos.model.Direccion
import com.example.tg_persistenciadatos.model.Propietario
import com.example.tg_persistenciadatos.model.Vivienda
import com.example.tg_persistenciadatos.model.ViviendaCaracteristicaCrossRef

@Dao
interface ViviendaDao {

    @Query("SELECT * FROM viviendas")
    suspend fun getAllViviendas(): List<Vivienda>

    @Query("SELECT * FROM propietarios")
    suspend fun getAllPropietarios(): List<Propietario>

    // Añade esto en ViviendaDao.kt
    @Query("SELECT * FROM caracteristicas")
    suspend fun getAllCaracteristicas(): List<com.example.tg_persistenciadatos.model.Caracteristica>

    // Esta es la consulta clave que faltaba para las etiquetas
    @Query("""
        SELECT nombre FROM caracteristicas 
        INNER JOIN vivienda_caracteristica_cruce
        ON caracteristicas.id = vivienda_caracteristica_cruce.caracteristicaId 
        WHERE vivienda_caracteristica_cruce.viviendaId = :viviendaId
    """)
    suspend fun getEtiquetasPorVivienda(viviendaId: Int): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertViviendas(viviendas: List<Vivienda>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVivienda(vivienda: Vivienda)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropietarios(propietarios: List<Propietario>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirecciones(direcciones: List<Direccion>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDireccion(direccion: Direccion)

    // Añade esto en tu ViviendaDao
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCaracteristica(caracteristica: Caracteristica)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaracteristicas(caracteristicas: List<com.example.tg_persistenciadatos.model.Caracteristica>)

    @Query("DELETE FROM caracteristicas")
    suspend fun deleteAllCaracteristicas()

    @Query("DELETE FROM vivienda_caracteristica_cruce")
    suspend fun deleteAllCrossRefs()

    @Query("SELECT id FROM caracteristicas WHERE nombre = :nombre LIMIT 1")
    suspend fun getCaracteristicaIdPorNombre(nombre: String): Int?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossRef(crossRef: ViviendaCaracteristicaCrossRef)

    @Update
    suspend fun updateVivienda(vivienda: Vivienda)

    @Delete
    suspend fun deleteVivienda(vivienda: Vivienda)

    @Query("DELETE FROM viviendas")
    suspend fun deleteAllViviendas()

    @Query("DELETE FROM propietarios")
    suspend fun deleteAllPropietarios()

    @Query("DELETE FROM direcciones")
    suspend fun deleteAllDirecciones()

}