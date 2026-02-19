package com.example.tg_persistenciadatos.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tg_persistenciadatos.data.repository.ViviendaRepository
import com.example.tg_persistenciadatos.model.Vivienda
import kotlinx.coroutines.launch

class ViviendaViewModel(private val repository: ViviendaRepository) : ViewModel() {

    var listaViviendas by mutableStateOf<List<Vivienda>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            isLoading = true
            // Intentamos sincronizar con la API, si falla usamos local
            repository.refreshDatos()
            listaViviendas = repository.obtenerViviendasLocales()
            isLoading = false
        }
    }

    // --- NUEVO: Obtener una vivienda específica para editarla ---
    fun getVivienda(id: Int): Vivienda? {
        return listaViviendas.find { it.id == id }
    }

    // --- NUEVO: Agregar con datos reales ---
    fun agregarVivienda(titulo: String, precio: Double, imagen: String, propietarioId: Int) {
        viewModelScope.launch {
            // Generamos ID temporal random (la API debería asignarlo idealmente)
            val nueva = Vivienda(
                id = (1000..9999).random(),
                titulo = titulo,
                precio = precio,
                imagen = imagen,
                propietarioId = propietarioId
            )
            repository.agregar(nueva)
            cargarDatos()
        }
    }

    // --- NUEVO: Actualizar con datos reales ---
    fun actualizarVivienda(id: Int, titulo: String, precio: Double, imagen: String, propietarioId: Int) {
        viewModelScope.launch {
            val viviendaEditada = Vivienda(
                id = id,
                titulo = titulo,
                precio = precio,
                imagen = imagen,
                propietarioId = propietarioId
            )
            repository.actualizar(viviendaEditada)
            cargarDatos()
        }
    }

    fun borrarVivienda(vivienda: Vivienda) {
        viewModelScope.launch {
            repository.borrar(vivienda)
            cargarDatos()
        }
    }
}