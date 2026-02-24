package com.example.tg_persistenciadatos.data.repository

import android.util.Log
import com.example.tg_persistenciadatos.api.RetrofitClient
import com.example.tg_persistenciadatos.data.local.ViviendaDao
import com.example.tg_persistenciadatos.model.Direccion
import com.example.tg_persistenciadatos.model.Vivienda

/**
 * Conecta la API de Retrofit con la bade de datos local ROOM
 */
class ViviendaRepository(private val dao: ViviendaDao) {

    //metodos para leer los datos locales
    suspend fun obtenerViviendasLocales(): List<Vivienda> = dao.getAllViviendas()

    suspend fun obtenerPropietariosLocales() = dao.getAllPropietarios()

    suspend fun obtenerCaracteristicasLocales() = dao.getAllCaracteristicas()

    suspend fun obtenerEtiquetasDeVivienda(viviendaId: Int): List<String> {
        return dao.getEtiquetasPorVivienda(viviendaId)
    }

    //Sincroniza la base de datos con la API, borra la base de datos antigua para insertar una nueva
    suspend fun refreshDatos() {
        try {
            //Descarga informacion de la API
            val resV = RetrofitClient.instance.getViviendas()
            val resP = RetrofitClient.instance.getPropietarios()
            val resD = RetrofitClient.instance.getDirecciones()
            val resC = RetrofitClient.instance.getCaracteristicas()

            if (resV.isSuccessful && resP.isSuccessful && resD.isSuccessful && resC.isSuccessful) {
                //Borra los datos locales antiguos (sin esto la aplicacion solo abría una vez)
                dao.deleteAllCrossRefs()
                dao.deleteAllCaracteristicas()
                dao.deleteAllViviendas()
                dao.deleteAllPropietarios()
                dao.deleteAllDirecciones()

                //Inserta entidades simples
                resP.body()?.let { dao.insertPropietarios(it) }
                resD.body()?.let { dao.insertDirecciones(it) }
                resC.body()?.let { dao.insertCaracteristicas(it) }

                //Inserta viviendas y construye la N:M con características
                resV.body()?.let { listaViviendas ->
                    dao.insertViviendas(listaViviendas)

                    listaViviendas.forEach { vivienda ->
                        vivienda.caracteristicasId.forEach { idCaract ->
                            val crossRef = com.example.tg_persistenciadatos.model.ViviendaCaracteristicaCrossRef(
                                viviendaId = vivienda.id,
                                caracteristicaId = idCaract
                            )
                            dao.insertCrossRef(crossRef)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("REPO", "Error en refreshDatos: ${e.message}")
        }
    }

    /**
     * Guarda nuevas viviendas creadas en el formulario de la app
     * Inserta los datos de ka dirección y las relaciones con la tabla de características
     */
    suspend fun guardarNuevaVivienda(vivienda: Vivienda, direccion: Direccion, caracteristicasIds: List<Int>) {
        dao.insertDireccion(direccion)
        dao.insertVivienda(vivienda)

        caracteristicasIds.forEach { idCaract ->
            val crossRef = com.example.tg_persistenciadatos.model.ViviendaCaracteristicaCrossRef(vivienda.id, idCaract)
            dao.insertCrossRef(crossRef)
        }
    }

    suspend fun actualizar(vivienda: Vivienda) = dao.updateVivienda(vivienda)

    suspend fun borrar(vivienda: Vivienda) = dao.deleteVivienda(vivienda)
}