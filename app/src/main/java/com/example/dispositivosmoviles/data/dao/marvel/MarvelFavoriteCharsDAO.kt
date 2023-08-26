package com.example.dispositivosmoviles.data.dao.marvel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelFavoriteCharsDB

@Dao
interface MarvelFavoriteCharsDAO {

    @Query("select * from MarvelFavoriteChars")
    fun getAllCharacters() : List<MarvelFavoriteCharsDB>

    @Query("select * from MarvelFavoriteChars where id = :pk")
    fun getOneCharacters(pk: Int): MarvelFavoriteCharsDB

    @Insert
    fun insertMarvelChar(ch: MarvelFavoriteCharsDB)

    @Delete
    fun deleteMarvelChar(ch: MarvelFavoriteCharsDB)
}