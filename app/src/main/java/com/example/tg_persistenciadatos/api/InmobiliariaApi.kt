package com.example.tg_persistenciadatos.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface InmobiliariaApi {

    // --- VIVIENDAS ---
    @GET("vivienda")
    suspend fun getViviendas(): Response<List<Vivienda>>

    @POST("vivienda")
    suspend fun createVivienda(@Body vivienda: Vivienda): Response<Vivienda>

    // --- CARACTERÍSTICAS (Antes Etiquetas) ---
    @GET("caracteristica")
    suspend fun getCaracteristicas(): Response<List<Caracteristica>>

    // --- PROPIETARIOS ---
    @GET("propietario")
    suspend fun getPropietarios(): Response<List<Propietario>>

    // --- DIRECCIONES ---
    // Necesario para la lista (opcional si no la usas en otro lado)
    @GET("direccion")
    suspend fun getDirecciones(): Response<List<Direccion>>

    // IMPORTANTE: Necesario para crear la dirección antes de la vivienda
    @POST("direccion")
    suspend fun createDireccion(@Body direccion: Direccion): Response<Direccion>
}