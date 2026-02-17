package com.example.tg_persistenciadatos.data.remote

import androidx.compose.ui.graphics.vector.Path
import com.example.tg_persistenciadatos.model.Caracteristica
import com.example.tg_persistenciadatos.model.Direccion
import com.example.tg_persistenciadatos.model.Propietario
import com.example.tg_persistenciadatos.model.Vivienda
import com.example.tg_persistenciadatos.model.ViviendaCaracteristicaCrossRef
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ViviendaApiService {

    // Endpoints coinciden con tu db.json

    @GET("viviendas")
    suspend fun getViviendas(): List<Vivienda>

    @GET("propietarios")
    suspend fun getPropietarios(): List<Propietario>

    @GET("direcciones")
    suspend fun getDirecciones(): List<Direccion>

    @GET("caracteristicas")
    suspend fun getCaracteristicas(): List<Caracteristica>

    @GET("vivienda_caracteristicas")
    suspend fun getCruce(): List<ViviendaCaracteristicaCrossRef>

    @POST("viviendas")
    suspend fun addVivienda(@Body vivienda: Vivienda): Vivienda

    @PUT("viviendas/{id}")
    suspend fun updateVivienda(@Path("id") id: Int, @Body vivienda: Vivienda): Vivienda

    @DELETE("viviendas/{id}")
    suspend fun deleteVivienda(@Path("id") id: Int)
}