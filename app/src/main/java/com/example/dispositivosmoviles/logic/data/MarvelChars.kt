package com.example.dispositivosmoviles.logic.data

import android.os.Parcelable
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelCharsDB
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelFavoriteCharsDB
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarvelChars(
    val id: Int,
    val name: String,
    val comic: String,
   // val description: String,
    val image: String
) : Parcelable

fun MarvelChars.getMarvelCharsDB() : MarvelCharsDB{
    return MarvelCharsDB(
        id,
        name,
        comic,
       // description,
        image
    )
}

fun MarvelChars.getFavoriteMarvelCharsDB() : MarvelFavoriteCharsDB{
    return MarvelFavoriteCharsDB(
        id,
        name,
        comic,
        //description,
        image
    )
}
