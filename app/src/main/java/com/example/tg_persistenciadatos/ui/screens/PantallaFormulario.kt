package com.example.tg_persistenciadatos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tg_persistenciadatos.ui.viewmodel.ViviendaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormulario(navController: NavController, viewModel: ViviendaViewModel, viviendaId: Int) {
    val esEdicion = viviendaId != -1
    val viviendaExistente = if (esEdicion) viewModel.getVivienda(viviendaId) else null

    var modelo by remember { mutableStateOf(viviendaExistente?.modelo ?: "") }
    var precioStr by remember { mutableStateOf(viviendaExistente?.precio?.toString() ?: "") }
    var expanded by remember { mutableStateOf(false) }
    val propietarios = viewModel.listaPropietarios
    var propSeleccionado by remember {
        mutableStateOf(propietarios.find { it.id == viviendaExistente?.propietarioId } ?: propietarios.firstOrNull())
    }

    var calle by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var piso by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (esEdicion) "Editar" else "Nueva Vivienda") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            OutlinedTextField(value = modelo, onValueChange = { modelo = it }, label = { Text("Modelo") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = precioStr, onValueChange = { precioStr = it }, label = { Text("Precio") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            Text("Propietario:")
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = propSeleccionado?.nombre ?: "Seleccionar...",
                    onValueChange = {}, readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    propietarios.forEach { prop ->
                        DropdownMenuItem(text = { Text(prop.nombre) }, onClick = { propSeleccionado = prop; expanded = false })
                    }
                }
            }

            if (!esEdicion) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Dirección:", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(value = calle, onValueChange = { calle = it }, label = { Text("Calle") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = ciudad, onValueChange = { ciudad = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = piso, onValueChange = { piso = it }, label = { Text("Piso") }, modifier = Modifier.fillMaxWidth())
            }

            Button(
                modifier = Modifier.padding(top = 24.dp).fillMaxWidth(),
                onClick = {
                    val p = precioStr.toIntOrNull() ?: 0
                    if (esEdicion) {
                        viewModel.actualizarVivienda(viviendaId, modelo, p, propSeleccionado?.id ?: 0, viviendaExistente?.direccionId ?: 0)
                    } else {
                        viewModel.guardarViviendaCompleta(modelo, p, propSeleccionado?.id ?: 0, calle, ciudad, piso)
                    }
                    navController.popBackStack()
                }
            ) { Text("Guardar") }
        }
    }
}