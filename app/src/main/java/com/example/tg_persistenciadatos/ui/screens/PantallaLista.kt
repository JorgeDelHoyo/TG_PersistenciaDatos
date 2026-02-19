package com.example.tg_persistenciadatos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tg_persistenciadatos.model.Vivienda
import com.example.tg_persistenciadatos.ui.viewmodel.ViviendaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(
    navController: NavController,
    viewModel: ViviendaViewModel
) {
    // Observamos los estados del ViewModel
    val viviendas = viewModel.listaViviendas
    val isLoading = viewModel.isLoading

    // Estado local para el diálogo de borrar
    var viviendaAEliminar by remember { mutableStateOf<Vivienda?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inmuebles") },
                actions = {
                    // Botón para forzar sincronización con la API
                    IconButton(onClick = { viewModel.actualizarTodo() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                    }
                }
            )
        },
        floatingActionButton = {
            // Navega al formulario con id -1 para CREAR
            FloatingActionButton(onClick = { navController.navigate("formulario/-1") }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { padding ->
        // Si está cargando y no hay datos, mostramos un círculo de progreso
        if (isLoading && viviendas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viviendas) { vivienda ->
                    Card(
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(vivienda.modelo, style = MaterialTheme.typography.titleLarge)
                            Text(
                                "Precio: ${vivienda.precio} €",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                // BOTÓN EDITAR
                                IconButton(onClick = {
                                    navController.navigate("formulario/${vivienda.id}")
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                                }

                                // BOTÓN BORRAR
                                IconButton(onClick = {
                                    viviendaAEliminar = vivienda
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Diálogo de confirmación para borrar
        viviendaAEliminar?.let { vivienda ->
            AlertDialog(
                onDismissRequest = { viviendaAEliminar = null },
                title = { Text("Eliminar") },
                text = { Text("¿Deseas borrar '${vivienda.modelo}'?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.borrarVivienda(vivienda)
                            viviendaAEliminar = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Confirmar", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viviendaAEliminar = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}