package com.example.tg_persistenciadatos

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tg_persistenciadatos.api.*
import com.example.tg_persistenciadatos.ui.theme.TG_PersistenciaDatosTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TG_PersistenciaDatosTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "lista",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("lista") {
                            PantallaLista(
                                onNavigateToCreate = { navController.navigate("crear") }
                            )
                        }
                        composable("crear") {
                            PantallaCrearVivienda(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------
// PANTALLA 1: LISTADO (AHORA CON BOTONES PUT Y DELETE)
// -----------------------------------------------------------
@Composable
fun PantallaLista(onNavigateToCreate: () -> Unit) {
    var listaViviendas by remember { mutableStateOf(emptyList<Vivienda>()) }
    var listaCaracteristicas by remember { mutableStateOf(emptyList<Caracteristica>()) }
    var cargando by remember { mutableStateOf(true) }

    // Necesitamos el contexto y corrutinas para los botones de las tarjetas
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            val resViviendas = RetrofitClient.instance.getViviendas()
            val resCaracteristicas = RetrofitClient.instance.getCaracteristicas()

            if (resViviendas.isSuccessful) listaViviendas = resViviendas.body() ?: emptyList()
            if (resCaracteristicas.isSuccessful) listaCaracteristicas = resCaracteristicas.body() ?: emptyList()
        } catch (e: Exception) {
            Toast.makeText(context, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            cargando = false
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreate) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Inmobiliaria", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))

            if (cargando) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn {
                    items(listaViviendas) { vivienda ->
                        val nombresCaracteristicas = vivienda.caracteristicaIds.mapNotNull { id ->
                            listaCaracteristicas.find { it.id == id }?.nombre
                        }.joinToString(", ")

                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Columna para los datos de la vivienda
                                Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                                    Text(vivienda.modelo, style = MaterialTheme.typography.titleMedium)
                                    Text("${vivienda.precio} €", style = MaterialTheme.typography.bodyLarge)
                                    Text("Características: $nombresCaracteristicas", style = MaterialTheme.typography.bodySmall)
                                }

                                // Columna para los botones (PUT y DELETE)
                                Column(modifier = Modifier.padding(end = 8.dp)) {

                                    // BOTÓN PUT (Editar): Sumamos 100€ al precio para probar
                                    IconButton(onClick = {
                                        scope.launch {
                                            try {
                                                val viviendaModificada = vivienda.copy(
                                                    precio = vivienda.precio + 100,
                                                    modelo = vivienda.modelo + " *"
                                                )
                                                val resp = RetrofitClient.instance.updateVivienda(vivienda.id, viviendaModificada)

                                                if (resp.isSuccessful) {
                                                    // Actualizamos la lista local para ver el cambio en pantalla
                                                    listaViviendas = listaViviendas.map {
                                                        if (it.id == vivienda.id) viviendaModificada else it
                                                    }
                                                    Toast.makeText(context, "Actualizado (PUT)", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    Toast.makeText(context, "Error PUT: ${resp.code()}", Toast.LENGTH_SHORT).show()
                                                }
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Fallo: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                                    }

                                    // BOTÓN DELETE (Borrar)
                                    IconButton(onClick = {
                                        scope.launch {
                                            try {
                                                val resp = RetrofitClient.instance.deleteVivienda(vivienda.id)
                                                if (resp.isSuccessful) {
                                                    // Quitamos la vivienda de la lista local
                                                    listaViviendas = listaViviendas.filter { it.id != vivienda.id }
                                                    Toast.makeText(context, "Borrado (DELETE)", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    Toast.makeText(context, "Error DELETE: ${resp.code()}", Toast.LENGTH_SHORT).show()
                                                }
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Fallo: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------
// PANTALLA 2: FORMULARIO (Mantenida exactamente igual)
// -----------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearVivienda(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Datos Vivienda
    var modelo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    // Datos Dirección
    var dirCalle by remember { mutableStateOf("") }
    var dirCiudad by remember { mutableStateOf("") }
    var dirPiso by remember { mutableStateOf("") }

    // Listas para desplegables
    var listaPropietarios by remember { mutableStateOf(emptyList<Propietario>()) }
    var listaCaracteristicas by remember { mutableStateOf(emptyList<Caracteristica>()) }

    // Selecciones
    var propietarioSeleccionado by remember { mutableStateOf<Propietario?>(null) }
    val caracteristicasSeleccionadas = remember { mutableStateListOf<Caracteristica>() }

    // Control UI
    var expandProp by remember { mutableStateOf(false) }
    var expandCar by remember { mutableStateOf(false) }
    var guardando by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val rProp = RetrofitClient.instance.getPropietarios()
        val rCar = RetrofitClient.instance.getCaracteristicas()

        if (rProp.isSuccessful) listaPropietarios = rProp.body() ?: emptyList()
        if (rCar.isSuccessful) listaCaracteristicas = rCar.body() ?: emptyList()
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text("Nueva Vivienda", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // SECCION 1: VIVIENDA
        Text("Datos Generales", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        OutlinedTextField(value = modelo, onValueChange = { modelo = it }, label = { Text("Modelo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio (€)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        // SECCION 2: DIRECCIÓN
        Text("Nueva Dirección", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(8.dp)) {
                OutlinedTextField(value = dirCalle, onValueChange = { dirCalle = it }, label = { Text("Calle") }, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = dirCiudad, onValueChange = { dirCiudad = it }, label = { Text("Ciudad") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = dirPiso, onValueChange = { dirPiso = it }, label = { Text("Piso") }, modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECCION 3: PROPIETARIO
        Text("Propietario", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        ExposedDropdownMenuBox(expanded = expandProp, onExpandedChange = { expandProp = !expandProp }) {
            OutlinedTextField(
                value = propietarioSeleccionado?.nombre ?: "Selecciona Propietario",
                onValueChange = {}, readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandProp) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandProp, onDismissRequest = { expandProp = false }) {
                listaPropietarios.forEach { prop ->
                    DropdownMenuItem(text = { Text(prop.nombre) }, onClick = { propietarioSeleccionado = prop; expandProp = false })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECCION 4: CARACTERÍSTICAS
        Text("Características: " + caracteristicasSeleccionadas.joinToString { it.nombre }, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        ExposedDropdownMenuBox(expanded = expandCar, onExpandedChange = { expandCar = !expandCar }) {
            OutlinedTextField(
                value = "Añadir característica...", onValueChange = {}, readOnly = true,
                trailingIcon = { Icon(Icons.Default.Add, contentDescription = null) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandCar, onDismissRequest = { expandCar = false }) {
                listaCaracteristicas.forEach { car ->
                    DropdownMenuItem(text = { Text(car.nombre) }, onClick = {
                        if (!caracteristicasSeleccionadas.contains(car)) caracteristicasSeleccionadas.add(car)
                        expandCar = false
                    })
                }
            }
        }
        if (caracteristicasSeleccionadas.isNotEmpty()) {
            TextButton(onClick = { caracteristicasSeleccionadas.clear() }) { Text("Limpiar características") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // BOTÓN GUARDAR
        Button(
            enabled = !guardando,
            onClick = {
                scope.launch {
                    guardando = true
                    try {
                        if (propietarioSeleccionado == null) {
                            Toast.makeText(context, "Selecciona un propietario", Toast.LENGTH_SHORT).show()
                            guardando = false
                            return@launch
                        }

                        val nuevaDireccion = Direccion(
                            id = 0,
                            calle = dirCalle,
                            ciudad = dirCiudad,
                            piso = dirPiso
                        )

                        val respDir = RetrofitClient.instance.createDireccion(nuevaDireccion)

                        if (respDir.isSuccessful && respDir.body() != null) {
                            val direccionCreada = respDir.body()!!
                            val idDireccionReal = direccionCreada.id

                            val nuevaVivienda = Vivienda(
                                id = 0,
                                modelo = modelo,
                                precio = precio.toIntOrNull() ?: 0,
                                propietarioId = propietarioSeleccionado!!.id,
                                direccionId = idDireccionReal,
                                caracteristicaIds = caracteristicasSeleccionadas.map { it.id }
                            )

                            val respViv = RetrofitClient.instance.createVivienda(nuevaVivienda)

                            if (respViv.isSuccessful) {
                                Toast.makeText(context, "¡Vivienda y Dirección guardadas!", Toast.LENGTH_LONG).show()
                                onNavigateBack()
                            } else {
                                Toast.makeText(context, "Error al guardar vivienda: ${respViv.code()}", Toast.LENGTH_LONG).show()
                            }

                        } else {
                            Toast.makeText(context, "Error al guardar dirección: ${respDir.code()}", Toast.LENGTH_LONG).show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(context, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                    } finally {
                        guardando = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (guardando) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardando...")
            } else {
                Text("GUARDAR TODO")
            }
        }
    }
}