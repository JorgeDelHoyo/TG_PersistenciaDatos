package com.example.tg_persistenciadatos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tg_persistenciadatos.model.*

@Database(
    entities = [
        Vivienda::class,
        Propietario::class,
        Direccion::class,
        Caracteristica::class,
        ViviendaCaracteristicaCrossRef::class // <-- CAMBIADO de Cruce a CrossRef
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ViviendaDatabase : RoomDatabase() {

    abstract fun viviendaDao(): ViviendaDao

    companion object {
        @Volatile
        private var INSTANCE: ViviendaDatabase? = null

        fun getDatabase(context: Context): ViviendaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ViviendaDatabase::class.java,
                    "vivienda_database"
                )
                    // Esto evita que la app se cierre si cambias el modelo de datos
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}