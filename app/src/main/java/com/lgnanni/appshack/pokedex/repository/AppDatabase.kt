package com.lgnanni.appshack.pokedex.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lgnanni.appshack.pokedex.repository.dao.PokemonListDao
import com.lgnanni.appshack.pokedex.repository.entity.PokemonListEntity

@Database(entities = [PokemonListEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonListDao(): PokemonListDao
}

object DatabaseInstance {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}