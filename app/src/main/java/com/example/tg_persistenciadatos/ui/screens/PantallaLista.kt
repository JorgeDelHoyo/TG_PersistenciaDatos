package com.example.tg_persistenciadatos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tg_persistenciadatos.model.Vivienda
import com.example.tg_persistenciadatos.ui.viewmodel.ViviendaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(
    navController: NavController,
    viewModel: ViviendaViewModel
) {
    val viviendas = viewModel.listaViviendas
    var viviendaAEliminar by remember { mutableStateOf<Vivienda?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inmobiliaria Pro") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("formulario/-1") }, // -1 = NUEVA
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { padding ->

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(viviendas) { vivienda ->
                ItemVivienda(
                    vivienda = vivienda,
                    onEditClick = { navController.navigate("formulario/${vivienda.id}") }, // Pasamos ID
                    onDeleteClick = { viviendaAEliminar = vivienda }
                )
            }
        }

        // DIÁLOGO DE CONFIRMACIÓN
        if (viviendaAEliminar != null) {
            AlertDialog(
                onDismissRequest = { viviendaAEliminar = null },
                title = { Text("Confirmar borrado") },
                text = { Text("¿Seguro que quieres borrar '${viviendaAEliminar?.titulo}'?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viviendaAEliminar?.let { viewModel.borrarVivienda(it) }
                            viviendaAEliminar = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Borrar") }
                },
                dismissButton = {
                    TextButton(onClick = { viviendaAEliminar = null }) { Text("Cancelar") }
                }
            )
        }
    }
}

@Composable
fun ItemVivienda(
    vivienda: Vivienda,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column {
            AsyncImage(
                model = vivienda.imagen,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(vivienda.titulo, style = MaterialTheme.typography.titleMedium)
                Text("${vivienda.precio} €", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    FilledTonalButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Editar")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = onDeleteClick, colors = ButtonDefaults.buttonColors(Color.Red)) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}