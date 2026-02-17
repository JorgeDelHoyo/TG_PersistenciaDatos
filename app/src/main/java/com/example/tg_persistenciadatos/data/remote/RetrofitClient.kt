package com.example.tg_persistenciadatos.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitClient {

    // IP ESPECIAL PARA EL EMULADOR (Apunta al localhost de tu PC)
    private const val BASE_URL = "http://10.0.2.2:3000/"

    // Configuración para que el JSON sea indulgente (ignore campos desconocidos si los hay)
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    // Cliente HTTP con logs (para ver en el Logcat qué está pasando)
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Veremos todo el JSON en el log
        })
        .build()

    // La instancia de Retrofit
    val instance: ViviendaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ViviendaApiService::class.java)
    }
}