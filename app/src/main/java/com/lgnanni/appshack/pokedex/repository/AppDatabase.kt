package com.lgnanni.appshack.pokedex.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lgnanni.appshack.pokedex.repository.dao.PokemonListDao
import com.lgnanni.appshack.pokedex.repository.entity.PokemonListEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [PokemonListEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonListDao(): PokemonListDao
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build()

    }

    @Provides
    fun providePokemonListDao(database: AppDatabase): PokemonListDao {
        return database.pokemonListDao()
    }
}