package com.example.dispositivosmoviles.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelFavoriteCharsDB
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.databinding.ActivityDatailsMarvelItemBinding
import com.example.dispositivosmoviles.ui.utilities.DispositivosMoviles
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsMarvelItem : AppCompatActivity() {

    private lateinit var binding: ActivityDatailsMarvelItemBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatailsMarvelItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
//        var name : String? = ""
//        intent.extras?.let {
//            name = it.getString("name")
//        }
//        if(!name.isNullOrEmpty()){
//            binding.txtName.text = name
//        }

        val item = intent.getParcelableExtra<MarvelChars>("name")

        if(item != null){
            binding.txtName.text = item.name
            binding.txtComic.text = item.comic
            Picasso.get().load(item.image).into(binding.imgMarvel)
            binding.btnGuardar.setOnClickListener{
                saveMarvelItem(item)
            }
        }

    }


    fun saveMarvelItem(item: MarvelChars):Boolean {
        val marvelSavedChar = MarvelFavoriteCharsDB(
            id = item.id,
            name = item.name,
            comic = item.comic,
            //description = item.description,
            image = item.image
        )

        val dao = DispositivosMoviles
            .getDbInstance()
            .marvelFavoriteDao()

        lifecycleScope.launch(Dispatchers.IO) {
            val exist = dao.getOneCharacters(item.id)
            if(exist != null){
                dao.deleteMarvelChar(exist)
                Snackbar.make(
                    binding.btnGuardar,
                    "Se elimino de favoritos",
                    Snackbar.LENGTH_LONG).show()
            }else{
                dao.insertMarvelChar(marvelSavedChar)
                Snackbar.make(
                    binding.btnGuardar,
                    "Se agrego a favoritos",
                    Snackbar.LENGTH_LONG).show()
            }
        }
        return false
    }


}