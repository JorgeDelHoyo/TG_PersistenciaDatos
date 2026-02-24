package com.example.tg_persistenciadatos.data.repository

import android.util.Log
import com.example.tg_persistenciadatos.api.RetrofitClient
import com.example.tg_persistenciadatos.data.local.ViviendaDao
import com.example.tg_persistenciadatos.model.Direccion
import com.example.tg_persistenciadatos.model.Vivienda

class ViviendaRepository(private val dao: ViviendaDao) {

    suspend fun obtenerViviendasLocales(): List<Vivienda> = dao.getAllViviendas()

    suspend fun obtenerPropietariosLocales() = dao.getAllPropietarios()

    suspend fun obtenerEtiquetasDeVivienda(viviendaId: Int): List<String> {
        return dao.getEtiquetasPorVivienda(viviendaId)
    }

    suspend fun refreshDatos() {
        try {
            val resV = RetrofitClient.instance.getViviendas()
            val resP = RetrofitClient.instance.getPropietarios()
            val resD = RetrofitClient.instance.getDirecciones()
            val resC = RetrofitClient.instance.getCaracteristicas()

            if (resV.isSuccessful && resP.isSuccessful && resD.isSuccessful && resC.isSuccessful) {

                dao.deleteAllCrossRefs()
                dao.deleteAllCaracteristicas()
                dao.deleteAllViviendas()
                dao.deleteAllPropietarios()
                dao.deleteAllDirecciones()

                resP.body()?.let { dao.insertPropietarios(it) }
                resD.body()?.let { dao.insertDirecciones(it) }
                resC.body()?.let { dao.insertCaracteristicas(it) }

                // --- NUEVA LÓGICA DE VINCULACIÓN ---
                resV.body()?.let { listaViviendas ->
                    dao.insertViviendas(listaViviendas)

                    // Recorremos las viviendas descargadas
                    listaViviendas.forEach { vivienda ->
                        // Recorremos su lista de características (ej: [5, 6])
                        vivienda.caracteristicasId.forEach { idCaract ->
                            // Creamos la relación en nuestra base de datos local
                            val crossRef = com.example.tg_persistenciadatos.model.ViviendaCaracteristicaCrossRef(
                                viviendaId = vivienda.id,
                                caracteristicaId = idCaract
                            )
                            dao.insertCrossRef(crossRef)
                        }
                    }
                }
                // ------------------------------------
            }
        } catch (e: Exception) {
            android.util.Log.e("REPO", "Error en refreshDatos: ${e.message}")
        }
    }

    suspend fun obtenerCaracteristicasLocales() = dao.getAllCaracteristicas()
    suspend fun guardarNuevaVivienda(vivienda: Vivienda, direccion: Direccion, caracteristicasIds: List<Int>) {
        dao.insertDireccion(direccion)
        dao.insertVivienda(vivienda)

        // Vinculamos la vivienda solo con los IDs de las características seleccionadas
        caracteristicasIds.forEach { idCaract ->
            val crossRef = com.example.tg_persistenciadatos.model.ViviendaCaracteristicaCrossRef(vivienda.id, idCaract)
            dao.insertCrossRef(crossRef)
        }
    }

    suspend fun actualizar(vivienda: Vivienda) = dao.updateVivienda(vivienda)

    suspend fun borrar(vivienda: Vivienda) = dao.deleteVivienda(vivienda)
}