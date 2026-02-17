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

    // Estado de la UI: Una lista de viviendas
    var listaViviendas by mutableStateOf<List<Vivienda>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        // Al crearse el ViewModel, cargamos los datos automáticamente
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            isLoading = true

            // 1. Intentamos actualizar desde internet
            repository.refreshDatos()

            // 2. Leemos lo que haya quedado en la base de datos
            listaViviendas = repository.obtenerViviendasLocales()

            isLoading = false
        }
    }
}