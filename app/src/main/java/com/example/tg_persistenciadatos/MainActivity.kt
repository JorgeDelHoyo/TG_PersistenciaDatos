package com.example.tg_persistenciadatos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tg_persistenciadatos.data.local.AppDatabase
import com.example.tg_persistenciadatos.data.repository.ViviendaRepository
import com.example.tg_persistenciadatos.ui.screens.PantallaFormulario
import com.example.tg_persistenciadatos.ui.screens.PantallaLista
import com.example.tg_persistenciadatos.ui.viewmodel.ViviendaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización de Dependencias (Room y ViewModel)
        val database = AppDatabase.getDatabase(this)
        val repository = ViviendaRepository(database.viviendaDao())

        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ViviendaViewModel(repository) as T
            }
        }

        setContent {
            val navController = rememberNavController()
            // Obtenemos el ViewModel una vez y lo compartimos entre pantallas
            val viewModel: ViviendaViewModel = viewModel(factory = viewModelFactory)

            NavHost(navController = navController, startDestination = "lista") {

                // RUTA 1: LISTA
                composable("lista") {
                    PantallaLista(navController, viewModel)
                }

                // RUTA 2: FORMULARIO (Recibe ID para editar o crear)
                composable(
                    route = "formulario/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: -1
                    PantallaFormulario(navController, viewModel, id)
                }
            }
        }
    }
}