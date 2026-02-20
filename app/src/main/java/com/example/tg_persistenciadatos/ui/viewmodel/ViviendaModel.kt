package com.example.tg_persistenciadatos.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tg_persistenciadatos.data.repository.ViviendaRepository
import com.example.tg_persistenciadatos.model.Direccion
import com.example.tg_persistenciadatos.model.Propietario
import com.example.tg_persistenciadatos.model.Vivienda
import kotlinx.coroutines.launch

class ViviendaViewModel(private val repository: ViviendaRepository) : ViewModel() {

    var listaViviendas by mutableStateOf<List<Vivienda>>(emptyList())
    var listaPropietarios by mutableStateOf<List<Propietario>>(emptyList())
    var mapaEtiquetas by mutableStateOf<Map<Int, List<String>>>(emptyMap())
    var isLoading by mutableStateOf(false)

    var listaCaracteristicas by mutableStateOf<List<com.example.tg_persistenciadatos.model.Caracteristica>>(emptyList())

    init {
        viewModelScope.launch {
            cargarDesdeLocal()
            actualizarTodo()
        }
    }

    fun actualizarTodo() {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.refreshDatos()
                cargarDesdeLocal()
            } catch (e: Exception) {
                Log.e("VM", "Error refrescando: ${e.message}")
            }
            isLoading = false
        }
    }

    suspend fun cargarDesdeLocal() {
        listaViviendas = repository.obtenerViviendasLocales()
        listaPropietarios = repository.obtenerPropietariosLocales()
        listaCaracteristicas = repository.obtenerCaracteristicasLocales()

        // Cargar etiquetas para cada vivienda
        val nuevoMapa = mutableMapOf<Int, List<String>>()
        listaViviendas.forEach { vivienda ->
            nuevoMapa[vivienda.id] = repository.obtenerEtiquetasDeVivienda(vivienda.id)
        }
        mapaEtiquetas = nuevoMapa
    }

    fun getVivienda(id: Int): Vivienda? = listaViviendas.find { it.id == id }

    fun actualizarVivienda(id: Int, modelo: String, precio: Int, propId: Int, dirId: Int) {
        viewModelScope.launch {
            repository.actualizar(Vivienda(id, modelo, precio, propId, dirId))
            cargarDesdeLocal()
        }
    }

    // Sustituye esta función en tu ViviendaViewModel
    fun guardarViviendaCompleta(modelo: String, precio: Int, propId: Int, calle: String, ciudad: String, piso: String, caracteristicasIds: List<Int>) {
        viewModelScope.launch {
            val dirId = (1000..9999).random()
            val vivId = (10000..99999).random()

            repository.guardarNuevaVivienda(
                Vivienda(vivId, modelo, precio, propId, dirId),
                Direccion(dirId, ciudad, calle, piso),
                caracteristicasIds // Pasamos los IDs seleccionados
            )
            cargarDesdeLocal()
        }
    }

    fun borrarVivienda(vivienda: Vivienda) {
        viewModelScope.launch {
            repository.borrar(vivienda)
            cargarDesdeLocal()
        }
    }
}