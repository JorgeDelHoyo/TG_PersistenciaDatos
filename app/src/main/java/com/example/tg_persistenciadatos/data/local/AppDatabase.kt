package com.example.tg_persistenciadatos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tg_persistenciadatos.model.Caracteristica
import com.example.tg_persistenciadatos.model.Direccion
import com.example.tg_persistenciadatos.model.Propietario
import com.example.tg_persistenciadatos.model.Vivienda
import com.example.tg_persistenciadatos.model.ViviendaCaracteristicaCrossRef

@Database(
    entities = [
        Vivienda::class,
        Propietario::class,
        Direccion::class,
        Caracteristica::class,
        ViviendaCaracteristicaCrossRef::class // ¡No olvides la tabla de cruce!
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun viviendaDao(): ViviendaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "inmobiliaria_db" // Nombre del archivo en el móvil
                )
                    .fallbackToDestructiveMigration() // Si cambias la BD, borra la vieja y crea una nueva (útil en desarrollo)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}