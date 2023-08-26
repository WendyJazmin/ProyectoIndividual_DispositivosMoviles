package com.example.dispositivosmoviles.logic.list

import com.example.dispositivosmoviles.data.entities.LoginUser
import com.example.dispositivosmoviles.logic.data.MarvelChars

class ListItems {

    fun returnItems(): List<LoginUser>{
        var items = listOf<LoginUser>(
            LoginUser("1", "1"),
            LoginUser("2", "2"),
            LoginUser("3", "3"),
            LoginUser("4", "4"),
            LoginUser("5", "5")
        )
        return items
    }

    fun returnMarvelChars() : List<MarvelChars>{
        val items = listOf(
            MarvelChars(
                1,
                "Spider-Man",
                "The Amazing Spider-Man",
                //"",
                "https://comicvine.gamespot.com/a/uploads/scale_small/12/124259/8126579-amazing_spider-man_vol_5_54_stormbreakers_variant_textless.jpg"
            ),
            MarvelChars(
                2,
                "Deadpool",
                "Deadpool max",
                //"",
                "https://comicvine.gamespot.com/a/uploads/scale_small/12/124259/8926324-large-2680196.jpg"
            ),
            MarvelChars(
                3,
                "Emma Frost",
                "X-Men Deluxe",
                //"",
                "https://comicvine.gamespot.com/a/uploads/scale_small/11174/111743204/8925171-emmafrost.jpg"
            ),
            MarvelChars(
                4,
                "Moon Knight",
                "Marc Spector: Moon Knight",
                //"",
                "https://comicvine.gamespot.com/a/uploads/scale_small/12/124259/8102817-moonknight.jpg"
            ),
            MarvelChars(
                5,
                "Black Cat",
                "Black Cat Origin",
                //"",
                "https://comicvine.gamespot.com/a/uploads/scale_small/11144/111442876/8759849-grr.jpg"
            ),
        )
        return items
    }
}