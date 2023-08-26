package com.example.dispositivosmoviles.data.connections

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiConnection {

    //opciones para que usuario escoja
    enum class typeApi {
        Jikan, Marvel, Pets
    }


    private val API_JIKAN = "https://api.jikan.moe/v4/"
    private val API_MARVEL = "https://gateway.marvel.com/v1/public/"


    private fun getConnection(base: String): Retrofit {
        var retrofit = Retrofit.Builder()
            .baseUrl(base)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit

    }

    //retorna el end point
    suspend fun <T, E:Enum<E>> getService(api: E,service : Class<T>): T {
        var BASE = ""
        when(api.name){
            typeApi.Jikan.name ->{
                BASE = API_JIKAN
            }

            typeApi.Marvel.name ->{
                BASE = API_MARVEL
            }
        }
        return getConnection(BASE).create(service)
    }


}