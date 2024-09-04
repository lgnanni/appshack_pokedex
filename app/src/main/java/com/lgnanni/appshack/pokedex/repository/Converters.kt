package com.lgnanni.appshack.pokedex.repository

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lgnanni.appshack.pokedex.model.Cries
import com.lgnanni.appshack.pokedex.model.OfficialSprites
import com.lgnanni.appshack.pokedex.model.PokemonDetails
import com.lgnanni.appshack.pokedex.model.PokemonListItem
import com.lgnanni.appshack.pokedex.model.SpeciesInfo
import com.lgnanni.appshack.pokedex.model.TypeSprite
import com.lgnanni.appshack.pokedex.repository.entity.PokemonDetailsEntity
import com.lgnanni.appshack.pokedex.repository.entity.PokemonListEntity

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromCries(cries: Cries): String {
        return gson.toJson(cries)
    }

    @TypeConverter
    fun toCries(criesString: String): Cries {
        val type = object : TypeToken<Cries>() {}.type
        return gson.fromJson(criesString, type)
    }

    @TypeConverter
    fun fromOfficialSprites(officialSprite: OfficialSprites): String {
        return gson.toJson(officialSprite)
    }

    @TypeConverter
    fun toOfficialSprites(officialSpriteString: String): OfficialSprites {
        val type = object : TypeToken<OfficialSprites>() {}.type
        return gson.fromJson(officialSpriteString, type)
    }

    @TypeConverter
    fun fromSpeciesInfo(speciesInfo: SpeciesInfo): String {
        return gson.toJson(speciesInfo)
    }

    @TypeConverter
    fun toSpeciesInfo(speciesInfoString: String): SpeciesInfo {
        val type = object : TypeToken<SpeciesInfo>() {}.type
        return gson.fromJson(speciesInfoString, type)
    }

    @TypeConverter
    fun fromTypeSprite(typeSprite: TypeSprite): String {
        return gson.toJson(typeSprite)
    }

    @TypeConverter
    fun toTypeSprite(typeSpriteString: String): TypeSprite {
        val type = object : TypeToken<TypeSprite>() {}.type
        return gson.fromJson(typeSpriteString, type)
    }
}

// Extension functions to convert between User and UserEntity
fun PokemonListEntity.toPokemonListItem() = PokemonListItem(name, url)
fun PokemonListItem.toPokemonListEntity() = PokemonListEntity(name, url)

fun PokemonDetailsEntity.toPokemonDetails() = PokemonDetails(id, name, cries, officialSprite, speciesInfo, typeSprite)
fun PokemonDetails.toPokemonDetailsEntity() = PokemonDetailsEntity(id, name, cries, officialSprite, speciesInfo, typeSprite)
