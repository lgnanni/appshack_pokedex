package com.lgnanni.appshack.pokedex.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lgnanni.appshack.pokedex.repository.dao.PokemonDetailsDao
import com.lgnanni.appshack.pokedex.repository.dao.PokemonListDao
import com.lgnanni.appshack.pokedex.repository.entity.PokemonDetailsEntity
import com.lgnanni.appshack.pokedex.repository.entity.PokemonListEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [PokemonListEntity::class, PokemonDetailsEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonListDao(): PokemonListDao
    abstract fun pokemonDetailsDao(): PokemonDetailsDao

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

    @Provides
    fun providePokemonDetailsDao(database: AppDatabase): PokemonDetailsDao {
        return database.pokemonDetailsDao()
    }
}