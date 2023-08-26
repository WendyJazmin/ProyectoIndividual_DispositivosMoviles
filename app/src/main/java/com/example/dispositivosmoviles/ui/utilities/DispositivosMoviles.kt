package com.example.dispositivosmoviles.ui.utilities

import android.app.Application
import androidx.room.Room
import com.example.dispositivosmoviles.data.connections.MarvelConnectionDB

class DispositivosMoviles : Application() {

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            MarvelConnectionDB::class.java,
            "marvelDB"
        ).build()

    }

    companion object{

        private var db : MarvelConnectionDB? = null

        fun getDbInstance() : MarvelConnectionDB {
            return db!!
        }

    }

}