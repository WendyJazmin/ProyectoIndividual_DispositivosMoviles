package com.example.dispositivosmoviles.data.entities.marvel.characters

import com.example.dispositivosmoviles.logic.data.MarvelChars


data class Result(
    val comics: Comics,
    val description: String,
    val events: Events,
    val id: Int,
    val modified: String,
    val name: String,
    val resourceURI: String,
    val series: Series,
    val stories: Stories,
    val thumbnail: Thumbnail,
    val urls: List<Url>
)

fun Result.getMarvelChars():MarvelChars{
    var comic: String = ""
    if(comics.items.isNotEmpty()){
        comic = comics.items[0].name
    }

    val a = MarvelChars(
        id,
        name,
        comic,
       // description,
        thumbnail.path+"."+thumbnail.extension
    )


    return a
}