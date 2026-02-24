package com.example.tg_persistenciadatos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.tg_persistenciadatos.data.local.ViviendaDatabase
import com.example.tg_persistenciadatos.data.repository.ViviendaRepository
import com.example.tg_persistenciadatos.ui.screens.*
import com.example.tg_persistenciadatos.ui.viewmodel.ViviendaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = ViviendaDatabase.getDatabase(this)
        val repository = ViviendaRepository(database.viviendaDao())
        val viewModel = ViviendaViewModel(repository)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "inicio") {
                    composable("inicio") { PantallaInicio(navController) }
                    composable("lista") { PantallaLista(navController, viewModel) }
                    composable("formulario/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {
                        PantallaFormulario(navController, viewModel, it.arguments?.getInt("id") ?: -1)
                    }
                }
            }
        }
    }
}