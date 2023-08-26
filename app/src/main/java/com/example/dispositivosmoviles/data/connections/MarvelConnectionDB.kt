package com.example.dispositivosmoviles.data.connections

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dispositivosmoviles.data.dao.marvel.MarvelCharsDAO
import com.example.dispositivosmoviles.data.dao.marvel.MarvelFavoriteCharsDAO
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelCharsDB
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelFavoriteCharsDB

@Database(
    entities = [MarvelCharsDB::class, MarvelFavoriteCharsDB::class],
    version = 2
)
abstract class MarvelConnectionDB : RoomDatabase(){

    abstract fun marvelDao() : MarvelCharsDAO
    abstract fun marvelFavoriteDao(): MarvelFavoriteCharsDAO

}