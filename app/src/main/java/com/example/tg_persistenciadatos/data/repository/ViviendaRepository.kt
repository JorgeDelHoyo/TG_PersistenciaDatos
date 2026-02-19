package com.example.tg_persistenciadatos.data.repository

import android.util.Log
import com.example.tg_persistenciadatos.api.RetrofitClient
import com.example.tg_persistenciadatos.data.local.ViviendaDao
import com.example.tg_persistenciadatos.model.Direccion
import com.example.tg_persistenciadatos.model.Vivienda
import com.example.tg_persistenciadatos.model.ViviendaCaracteristicaCrossRef

class ViviendaRepository(private val dao: ViviendaDao) {

    suspend fun obtenerViviendasLocales(): List<Vivienda> = dao.getAllViviendas()
    suspend fun obtenerPropietariosLocales() = dao.getAllPropietarios()

    suspend fun refreshDatos() {
        try {
            val resV = RetrofitClient.instance.getViviendas()
            val resP = RetrofitClient.instance.getPropietarios()
            val resD = RetrofitClient.instance.getDirecciones()

            if (resV.isSuccessful && resP.isSuccessful && resD.isSuccessful) {
                // Orden crucial: borrar hijos antes que padres
                dao.deleteAllViviendas()
                dao.deleteAllPropietarios()
                dao.deleteAllDirecciones()

                // Insertar datos nuevos
                resP.body()?.let { dao.insertPropietarios(it) }
                resD.body()?.let { dao.insertDirecciones(it) }
                resV.body()?.let { dao.insertViviendas(it) }
            }
        } catch (e: Exception) {
            Log.e("REPO", "Error en refresh: ${e.message}")
        }
    }

    suspend fun guardarNuevaVivienda(vivienda: Vivienda, direccion: Direccion) {
        dao.insertDireccion(direccion)
        dao.insertVivienda(vivienda)
    }

    suspend fun actualizar(vivienda: Vivienda) = dao.updateVivienda(vivienda)
    suspend fun borrar(vivienda: Vivienda) = dao.deleteVivienda(vivienda)
}