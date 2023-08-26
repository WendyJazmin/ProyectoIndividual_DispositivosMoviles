package com.example.dispositivosmoviles.logic.jikanLogic

import com.example.dispositivosmoviles.data.connections.ApiConnection
import com.example.dispositivosmoviles.data.endpoints.JikanEndpoint
import com.example.dispositivosmoviles.logic.data.MarvelChars

class JikanAnimeLogic {

    suspend fun getAllAnimes():List<MarvelChars>{

        //llamar endpoit base
        //var call = ApiConnection.getJiKanConnection()
        //realizar endpoint especifico
       //val response = call.create(JikanEndpoint::class.java).getAllAnimes()




        var itemList = arrayListOf<MarvelChars>()

        val response = ApiConnection.getService(
            ApiConnection.typeApi.Jikan,
            JikanEndpoint::class.java
        ).getAllAnimes()



        //si se ejecuto la respuesta
        if(response.isSuccessful){
            response.body()!!.data.forEach{
                val m=  MarvelChars(
                    it.mal_id,
                    it.title,
                    it.titles[0].title,
                   // it.title,
                    it.images.jpg.image_url
                )
                itemList.add(m)
            }
        }

        return itemList
    }
}