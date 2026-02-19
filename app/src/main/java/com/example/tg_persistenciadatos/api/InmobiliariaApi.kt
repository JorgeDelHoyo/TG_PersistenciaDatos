package com.example.tg_persistenciadatos.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface InmobiliariaApi {

    // ==========================================
    //                 VIVIENDAS
    // ==========================================

    @GET("vivienda")
    suspend fun getViviendas(): Response<List<Vivienda>>

    @POST("vivienda")
    suspend fun createVivienda(@Body vivienda: Vivienda): Response<Vivienda>

    // Actualizar una vivienda existente
    @PUT("vivienda/{id}")
    suspend fun updateVivienda(@Path("id") id: Int, @Body vivienda: Vivienda): Response<Vivienda>

    // Borrar una vivienda (Unit significa que no esperamos que el servidor devuelva datos, solo el código de éxito 200/204)
    @DELETE("vivienda/{id}")
    suspend fun deleteVivienda(@Path("id") id: Int): Response<Unit>


    // ==========================================
    //               DIRECCIONES
    // ==========================================

    @GET("direccion")
    suspend fun getDirecciones(): Response<List<Direccion>>

    @POST("direccion")
    suspend fun createDireccion(@Body direccion: Direccion): Response<Direccion>

    @PUT("direccion/{id}")
    suspend fun updateDireccion(@Path("id") id: Int, @Body direccion: Direccion): Response<Direccion>

    @DELETE("direccion/{id}")
    suspend fun deleteDireccion(@Path("id") id: Int): Response<Unit>


    // ==========================================
    //               PROPIETARIOS
    // ==========================================

    @GET("propietario")
    suspend fun getPropietarios(): Response<List<Propietario>>

    @POST("propietario")
    suspend fun createPropietario(@Body propietario: Propietario): Response<Propietario>

    @PUT("propietario/{id}")
    suspend fun updatePropietario(@Path("id") id: Int, @Body propietario: Propietario): Response<Propietario>

    @DELETE("propietario/{id}")
    suspend fun deletePropietario(@Path("id") id: Int): Response<Unit>


    // ==========================================
    //             CARACTERÍSTICAS
    // ==========================================

    @GET("caracteristica")
    suspend fun getCaracteristicas(): Response<List<Caracteristica>>

    @POST("caracteristica")
    suspend fun createCaracteristica(@Body caracteristica: Caracteristica): Response<Caracteristica>

    @PUT("caracteristica/{id}")
    suspend fun updateCaracteristica(@Path("id") id: Int, @Body caracteristica: Caracteristica): Response<Caracteristica>

    @DELETE("caracteristica/{id}")
    suspend fun deleteCaracteristica(@Path("id") id: Int): Response<Unit>
}