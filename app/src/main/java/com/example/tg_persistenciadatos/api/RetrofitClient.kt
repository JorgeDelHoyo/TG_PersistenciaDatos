package com.example.tg_persistenciadatos.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://my-json-server.typicode.com/DiegoHernandezIzquierdo/Prueba_API_Viviendas/"

    val instance: InmobiliariaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InmobiliariaApi::class.java)
    }
}