package com.example.tg_persistenciadatos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tg_persistenciadatos.ui.viewmodel.ViviendaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormulario(
    navController: NavController,
    viewModel: ViviendaViewModel,
    viviendaId: Int // -1 si es nueva, ID real si es editar
) {
    val esEdicion = viviendaId != -1
    // Si editamos, buscamos la vivienda. Si no, null.
    val viviendaExistente = if (esEdicion) viewModel.getVivienda(viviendaId) else null

    // Estados del formulario
    var titulo by remember { mutableStateOf(viviendaExistente?.titulo ?: "") }
    var precioStr by remember { mutableStateOf(viviendaExistente?.precio?.toString() ?: "") }
    var imagen by remember { mutableStateOf(viviendaExistente?.imagen ?: "https://loremflickr.com/320/240/house") }
    // Simplificación: Propietario ID fijo o editable (ponemos 1 por defecto)
    var propietarioIdStr by remember { mutableStateOf(viviendaExistente?.propietarioId?.toString() ?: "1") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (esEdicion) "Editar Vivienda" else "Nueva Vivienda") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CAMPO TÍTULO
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título / Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            // CAMPO PRECIO
            OutlinedTextField(
                value = precioStr,
                onValueChange = { precioStr = it },
                label = { Text("Precio (€)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // CAMPO URL IMAGEN
            OutlinedTextField(
                value = imagen,
                onValueChange = { imagen = it },
                label = { Text("URL Imagen") },
                modifier = Modifier.fillMaxWidth()
            )

            // CAMPO PROPIETARIO ID
            OutlinedTextField(
                value = propietarioIdStr,
                onValueChange = { propietarioIdStr = it },
                label = { Text("ID Propietario") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // BOTONES
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        val precioDouble = precioStr.toDoubleOrNull() ?: 0.0
                        val propIdInt = propietarioIdStr.toIntOrNull() ?: 1

                        if (esEdicion) {
                            viewModel.actualizarVivienda(viviendaId, titulo, precioDouble, imagen, propIdInt)
                        } else {
                            viewModel.agregarVivienda(titulo, precioDouble, imagen, propIdInt)
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}