package com.example.tg_persistenciadatos.data.remote

import com.example.tg_persistenciadatos.model.Caracteristica
import com.example.tg_persistenciadatos.model.Direccion
import com.example.tg_persistenciadatos.model.Propietario
import com.example.tg_persistenciadatos.model.Vivienda
import com.example.tg_persistenciadatos.model.ViviendaCaracteristicaCrossRef
import retrofit2.http.GET

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
}