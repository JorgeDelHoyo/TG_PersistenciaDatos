package com.example.tg_persistenciadatos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tg_persistenciadatos.R
import com.example.tg_persistenciadatos.model.Vivienda
import com.example.tg_persistenciadatos.ui.viewmodel.ViviendaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(navController: NavController, viewModel: ViviendaViewModel) {
    val viviendas = viewModel.listaViviendas
    val isLoading = viewModel.isLoading
    var viviendaAEliminar by remember { mutableStateOf<Vivienda?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inmuebles", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
                actions = {
                    TextButton(onClick = { viewModel.actualizarTodo() }) {
                        Text("Volver a cargar", color = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("formulario/-1") }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { padding ->
        if (isLoading && viviendas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.padding(padding).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viviendas) { vivienda ->
                    Card(
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(vivienda.modelo, style = MaterialTheme.typography.titleLarge)
                            Text("Precio: ${vivienda.precio} €", color = MaterialTheme.colorScheme.primary)

                            // --- ETIQUETAS (CHIPS) ---
                            val etiquetas = viewModel.mapaEtiquetas[vivienda.id] ?: emptyList()
                            if (etiquetas.isNotEmpty()) {
                                Spacer(Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    etiquetas.forEach { etiqueta ->
                                        SuggestionChip(
                                            onClick = {},
                                            label = { Text(etiqueta, style = MaterialTheme.typography.labelSmall) }
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { navController.navigate("formulario/${vivienda.id}") },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Text(" Editar")
                                }

                                Button(
                                    onClick = { viviendaAEliminar = vivienda },
                                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.eliminar)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Eliminar", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (viviendaAEliminar != null) {
            AlertDialog(
                onDismissRequest = { viviendaAEliminar = null },
                title = { Text("Confirmar borrado") },
                text = { Text("¿Estás seguro de eliminar esta vivienda?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.borrarVivienda(viviendaAEliminar!!)
                            viviendaAEliminar = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.eliminar))
                    ) { Text("Confirmar") }
                },
                dismissButton = { TextButton(onClick = { viviendaAEliminar = null }) { Text("Cancelar") } }
            )
        }
    }
}