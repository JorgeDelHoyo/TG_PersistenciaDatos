package com.example.tg_persistenciadatos.data.local

import androidx.room.*
import com.example.tg_persistenciadatos.model.Caracteristica
import com.example.tg_persistenciadatos.model.Direccion
import com.example.tg_persistenciadatos.model.Propietario
import com.example.tg_persistenciadatos.model.Vivienda
import com.example.tg_persistenciadatos.model.ViviendaCaracteristicaCrossRef

@Dao
interface ViviendaDao {

    //consultas get
    @Query("SELECT * FROM viviendas")
    suspend fun getAllViviendas(): List<Vivienda>

    @Query("SELECT * FROM propietarios")
    suspend fun getAllPropietarios(): List<Propietario>

    @Query("SELECT * FROM caracteristicas")
    suspend fun getAllCaracteristicas(): List<com.example.tg_persistenciadatos.model.Caracteristica>

    /**
     * Consulta Avanzada (JOIN):
     * Busca en la tabla intermedia las relaciones que coincidan con la viviendaId.
     * Luego, cruza esos datos con la tabla de características para sacar el nombre de la etiqueta.
     */
    @Query("""
        SELECT nombre FROM caracteristicas 
        INNER JOIN vivienda_caracteristica_cruce
        ON caracteristicas.id = vivienda_caracteristica_cruce.caracteristicaId 
        WHERE vivienda_caracteristica_cruce.viviendaId = :viviendaId
    """)
    suspend fun getEtiquetasPorVivienda(viviendaId: Int): List<String>

    //inserts
    // OnConflictStrategy.REPLACE actualiza el dato si el ID ya existe.
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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCaracteristica(caracteristica: Caracteristica)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaracteristicas(caracteristicas: List<com.example.tg_persistenciadatos.model.Caracteristica>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossRef(crossRef: ViviendaCaracteristicaCrossRef)

    //deletes
    //borrado masivo de datos para que no de problemas al cargar la api
    @Query("DELETE FROM caracteristicas")
    suspend fun deleteAllCaracteristicas()

    @Query("DELETE FROM vivienda_caracteristica_cruce")
    suspend fun deleteAllCrossRefs()

    @Query("SELECT id FROM caracteristicas WHERE nombre = :nombre LIMIT 1")
    suspend fun getCaracteristicaIdPorNombre(nombre: String): Int?

    @Query("DELETE FROM viviendas")
    suspend fun deleteAllViviendas()

    @Query("DELETE FROM propietarios")
    suspend fun deleteAllPropietarios()

    @Query("DELETE FROM direcciones")
    suspend fun deleteAllDirecciones()

    //actualizador de la vivienda
    @Update
    suspend fun updateVivienda(vivienda: Vivienda)

    //borrado de una vivienda concreta
    @Delete
    suspend fun deleteVivienda(vivienda: Vivienda)



}