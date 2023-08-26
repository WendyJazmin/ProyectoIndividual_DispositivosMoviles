package com.example.dispositivosmoviles.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener

import androidx.datastore.preferences.core.stringPreferencesKey

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelFavoriteCharsDB
import com.example.dispositivosmoviles.databinding.FragmentSecondBinding
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.logic.marvelLogic.MarvelLogic
import com.example.dispositivosmoviles.ui.activities.DetailsMarvelItem
import com.example.dispositivosmoviles.ui.activities.dataStore
import com.example.dispositivosmoviles.ui.adapters.MarvelAdapter
import com.example.dispositivosmoviles.ui.adapters.MarvelAdapter2
import com.example.dispositivosmoviles.ui.adapters.MarvelAdapter3
import com.example.dispositivosmoviles.ui.data.UserDataStore
import com.example.dispositivosmoviles.ui.utilities.DispositivosMoviles
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.map


class SecondFragment : Fragment() {

    private lateinit var binding: FragmentSecondBinding

    private lateinit var lManager: LinearLayoutManager

    private lateinit var rvAdapter: MarvelAdapter2

    private var marvelCharsItems: MutableList<MarvelChars> = mutableListOf<MarvelChars>()

    private lateinit var gManager: GridLayoutManager

    private var filterText: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentSecondBinding.inflate(
            layoutInflater, container, false
        )

        //da la disposicion de orientacion
        //sabe cuantos elementos han pasado
        lManager = LinearLayoutManager(
            requireActivity(), //contexto -> se pasa el contexto de la activity
            LinearLayoutManager.VERTICAL,
            false
        )

        gManager = GridLayoutManager(requireActivity(), 2)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch(Dispatchers.Main) {
            getDataStore()
                .collect { user ->
                    Log.d("UCE", user.email)
                    Log.d("UCE", user.name)
                    Log.d("UCE", user.session)
                }
        }

        val names = arrayListOf<String>("Carlos", "Xavier", "Andrés", "Pepe", "Mariano", "Rosa")

        val adapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.simple_layout,
            names
        )

        //binding.spinner.adapter = adapter
        //binding.listView.adapter = adapter

        chargeDataRV("")

        binding.rvSwipe2.setOnRefreshListener {
            chargeDataRV("")
            binding.rvSwipe2.isRefreshing = false
            gManager.scrollToPositionWithOffset(5, 20)
        }

        binding.rvMarvelChars2.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {

                        val v = gManager.childCount
                        val p = gManager.findFirstVisibleItemPosition()
                        val t = gManager.itemCount

                        // v
                        // p es la posicion en la que esta
                        // t es el total de items
                        if ((v + p) >= t) {
                            lifecycleScope.launch(Dispatchers.IO) {
                                //val newItems = JikanAnimeLogic().getAllAnimes()
                                val newItems = MarvelLogic().getMarvelChars(filterText, 99)

                                withContext(Dispatchers.Main) {
                                    rvAdapter.updateListItem(newItems)
                                }
                            }
                        }
                    }
                }
            }
        )

        binding.txtFilter2.addTextChangedListener { filteredText ->
            filterText = filteredText.toString()
            chargeDataRV(filterText)
        }

        chargeDataRV(filterText)


    }


    fun sendMarvelItem(item: MarvelChars) {
        val i = Intent(requireActivity(), DetailsMarvelItem::class.java)
        i.putExtra("name", item)
        startActivity(i)
    }

    fun saveMarvelItem(item: MarvelChars): Boolean {
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
            if (exist != null) {
                dao.deleteMarvelChar(exist)
                Snackbar.make(
                    binding.cardView2,
                    "Eliminado de favoritos",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                dao.insertMarvelChar(marvelSavedChar)
                Snackbar.make(
                    binding.cardView2,
                    "Agregado a favoritos",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
        return false
    }



    fun chargeDataRV(name: String) {

        lifecycleScope.launch(Dispatchers.Main) {

            marvelCharsItems = withContext(Dispatchers.IO) {
                return@withContext (MarvelLogic().getMarvelChars(name, 99))
            }

            rvAdapter = MarvelAdapter2(
                marvelCharsItems,
                fnClick = { sendMarvelItem(it) },
                fnSave = { saveMarvelItem(it) }
            )

            binding.rvMarvelChars2.apply {
                this.adapter = rvAdapter
                this.layoutManager = gManager
            }
        }
    }

    private fun getDataStore(): Flow<UserDataStore> =
        requireActivity().dataStore.data.map { prefs ->
            UserDataStore(
                name = prefs[stringPreferencesKey("usuario")].orEmpty(),
                email = prefs[stringPreferencesKey("email")].orEmpty(),
                session = prefs[stringPreferencesKey("session")].orEmpty()
            )
        }


}