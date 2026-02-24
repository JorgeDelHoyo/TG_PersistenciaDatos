package com.example.tg_persistenciadatos.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tg_persistenciadatos.model.Caracteristica
import com.example.tg_persistenciadatos.ui.viewmodel.ViviendaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormulario(navController: NavController, viewModel: ViviendaViewModel, viviendaId: Int) {
    val esEdicion = viviendaId != -1
    val viviendaExistente = if (esEdicion) viewModel.getVivienda(viviendaId) else null

    var modelo by remember { mutableStateOf(viviendaExistente?.modelo ?: "") }
    var precioStr by remember { mutableStateOf(viviendaExistente?.precio?.toString() ?: "") }
    var calle by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var piso by remember { mutableStateOf("") }

    var errModelo by remember { mutableStateOf(false) }
    var errPrecio by remember { mutableStateOf(false) }
    var errCalle by remember { mutableStateOf(false) }
    var errCiudad by remember { mutableStateOf(false) }
    var errPiso by remember { mutableStateOf(false) }

    val propietarios = viewModel.listaPropietarios
    var propSelected by remember { mutableStateOf(propietarios.find { it.id == viviendaExistente?.propietarioId } ?: propietarios.firstOrNull()) }
    var expandedProp by remember { mutableStateOf(false) }

    // --- ESTADOS PARA LAS ETIQUETAS ---
    val caracteristicasDisponibles = viewModel.listaCaracteristicas
    var etiquetasSeleccionadas by remember { mutableStateOf(listOf<Caracteristica>()) }
    var expandedEtiquetas by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (esEdicion) "Editar" else "Nueva Vivienda", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {

            OutlinedTextField(value = modelo, onValueChange = { modelo = it; errModelo = false }, label = { Text("Modelo") }, isError = errModelo, supportingText = { if (errModelo) Text("Obligatorio") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = precioStr, onValueChange = { precioStr = it; errPrecio = false }, label = { Text("Precio") }, isError = errPrecio, supportingText = { if (errPrecio) Text("Obligatorio") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

            if (!esEdicion) {
                Text("Dirección:", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp), style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(value = calle, onValueChange = { calle = it; errCalle = false }, label = { Text("Calle") }, isError = errCalle, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = ciudad, onValueChange = { ciudad = it; errCiudad = false }, label = { Text("Ciudad") }, isError = errCiudad, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = piso, onValueChange = { piso = it; errPiso = false }, label = { Text("Piso") }, isError = errPiso, modifier = Modifier.fillMaxWidth())

                // --- SELECTOR DE ETIQUETAS MÚLTIPLES ---
                Spacer(modifier = Modifier.height(16.dp))
                Text("Características:", style = MaterialTheme.typography.titleMedium)
                ExposedDropdownMenuBox(expanded = expandedEtiquetas, onExpandedChange = { expandedEtiquetas = !expandedEtiquetas }) {
                    OutlinedTextField(
                        value = "Añadir característica...",
                        onValueChange = {}, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEtiquetas) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedEtiquetas, onDismissRequest = { expandedEtiquetas = false }) {
                        // Filtramos para no mostrar las que ya están seleccionadas
                        val opcionesDisponibles = caracteristicasDisponibles.filter { it !in etiquetasSeleccionadas }

                        if (opcionesDisponibles.isEmpty()) {
                            DropdownMenuItem(text = { Text("No hay más opciones") }, onClick = { expandedEtiquetas = false })
                        } else {
                            opcionesDisponibles.forEach { caract ->
                                DropdownMenuItem(
                                    text = { Text(caract.nombre) },
                                    onClick = {
                                        etiquetasSeleccionadas = etiquetasSeleccionadas + caract
                                        expandedEtiquetas = false
                                    }
                                )
                            }
                        }
                    }
                }

                // --- MOSTRAR LAS BURBUJAS DE LO SELECCIONADO ---
                if (etiquetasSeleccionadas.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        etiquetasSeleccionadas.forEach { caract ->
                            InputChip(
                                selected = true,
                                onClick = { etiquetasSeleccionadas = etiquetasSeleccionadas - caract },
                                label = { Text(caract.nombre) },
                                trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Quitar", modifier = Modifier.size(16.dp)) }
                            )
                        }
                    }
                }
                // ---------------------------------------------
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Propietario:", style = MaterialTheme.typography.titleMedium)
            ExposedDropdownMenuBox(expanded = expandedProp, onExpandedChange = { expandedProp = !expandedProp }) {
                OutlinedTextField(
                    value = propSelected?.nombre ?: "Seleccionar...", onValueChange = {}, readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProp) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expandedProp, onDismissRequest = { expandedProp = false }) {
                    propietarios.forEach { prop ->
                        DropdownMenuItem(text = { Text(prop.nombre) }, onClick = { propSelected = prop; expandedProp = false })
                    }
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                onClick = {
                    errModelo = modelo.isBlank(); errPrecio = precioStr.isBlank()
                    if (!esEdicion) { errCalle = calle.isBlank(); errCiudad = ciudad.isBlank(); errPiso = piso.isBlank() }

                    val hasErrors = if (esEdicion) errModelo || errPrecio else errModelo || errPrecio || errCalle || errCiudad || errPiso

                    if (!hasErrors) {
                        val p = precioStr.toIntOrNull() ?: 0
                        if (esEdicion) {
                            viewModel.actualizarVivienda(viviendaId, modelo, p, propSelected?.id ?: 0, viviendaExistente?.direccionId ?: 0)
                        } else {
                            // Extraemos solo los IDs de las características que hemos seleccionado
                            val idsSeleccionados = etiquetasSeleccionadas.map { it.id }
                            viewModel.guardarViviendaCompleta(modelo, p, propSelected?.id ?: 0, calle, ciudad, piso, idsSeleccionados)
                        }
                        navController.popBackStack()
                    }
                }
            ) { Text("Guardar Datos") }
        }
    }
}