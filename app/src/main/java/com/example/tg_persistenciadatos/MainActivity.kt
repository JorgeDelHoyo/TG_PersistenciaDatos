package com.example.tg_persistenciadatos // Asegúrate de que sea tu paquete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tg_persistenciadatos.data.local.AppDatabase
import com.example.tg_persistenciadatos.data.repository.ViviendaRepository
import com.example.tg_persistenciadatos.ui.viewmodel.ViviendaViewModel
import com.example.tg_persistenciadatos.model.Vivienda

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicializar la Base de Datos
        val database = AppDatabase.getDatabase(this)

        // 2. Inicializar el Repositorio
        val repository = ViviendaRepository(database.viviendaDao())

        // 3. Crear la Factory para pasarle el repo al ViewModel
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ViviendaViewModel(repository) as T
            }
        }

        setContent {
            // 4. Obtener el ViewModel
            val viewModel: ViviendaViewModel = viewModel(factory = viewModelFactory)

            MaterialTheme {
                PantallaInicio(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(viewModel: ViviendaViewModel) {
    val viviendas = viewModel.listaViviendas
    val loading = viewModel.isLoading

    Scaffold(
        topBar = { TopAppBar(title = { Text("Inmobiliaria Pro") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            LazyColumn {
                items(viviendas) { vivienda ->
                    FilaVivienda(vivienda)
                }
            }
        }
    }
}

@Composable
fun FilaVivienda(vivienda: Vivienda) {
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = vivienda.titulo, style = MaterialTheme.typography.titleMedium)
            Text(text = "${vivienda.precio} €", style = MaterialTheme.typography.bodyLarge)
            // Aquí en el futuro mostraremos el nombre del dueño usando la relación
            Text(text = "Dueño ID: ${vivienda.propietarioId}", style = MaterialTheme.typography.bodySmall)
        }
    }
}