package com.example.tg_persistenciadatos.data.repository

import com.example.tg_persistenciadatos.data.local.ViviendaDao
import com.example.tg_persistenciadatos.data.remote.RetrofitClient
import com.example.tg_persistenciadatos.model.Vivienda

class ViviendaRepository(private val dao: ViviendaDao) {

    private val api = RetrofitClient.instance

    // Función maestra: Descarga TODO y lo guarda en local
    suspend fun refreshDatos() {
        try {
            // 1. Descargar de la API (en paralelo o secuencial)
            val propietarios = api.getPropietarios()
            val viviendas = api.getViviendas() // Aquí vienen los IDs, pero no los objetos completos
            val direcciones = api.getDirecciones()
            val caracteristicas = api.getCaracteristicas()
            val cruce = api.getCruce()

            // 2. Guardar en Room (Persistencia)
            // Borramos lo viejo para evitar duplicados raros si la API cambió mucho
            dao.deleteAll()

            dao.insertPropietarios(propietarios)
            dao.insertViviendas(viviendas)
            dao.insertDirecciones(direcciones)
            dao.insertCaracteristicas(caracteristicas)
            dao.insertCruce(cruce)

            println("DEBUG_REPO: Datos sincronizados correctamente")

        } catch (e: Exception) {
            println("DEBUG_REPO: Error al descargar datos: ${e.message}")
            // Aquí podríamos manejar el error (mostrar Toast, etc),
            // pero por ahora dejamos que se usen los datos viejos de la caché si existen.
        }
    }

    // La UI solo pide datos al DAO (Single Source of Truth)
    suspend fun obtenerViviendasLocales(): List<Vivienda> {
        return dao.getAllViviendas()
    }

    suspend fun agregar(vivienda: Vivienda) {
        val nuevaVivienda = api.addVivienda(vivienda) // La API nos devuelve la vivienda con ID real si fuera auto-generado
        dao.insert(nuevaVivienda)
    }

    suspend fun actualizar(vivienda: Vivienda) {
        api.updateVivienda(vivienda.id, vivienda)
        dao.update(vivienda)
    }

    suspend fun borrar(vivienda: Vivienda) {
        api.deleteVivienda(vivienda.id)
        dao.delete(vivienda)
    }
}