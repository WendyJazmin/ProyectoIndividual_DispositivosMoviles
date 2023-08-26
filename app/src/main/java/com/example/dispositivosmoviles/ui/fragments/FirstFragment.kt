package com.example.dispositivosmoviles.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelFavoriteCharsDB
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.databinding.FragmentFirstBinding
import com.example.dispositivosmoviles.logic.marvelLogic.MarvelLogic
import com.example.dispositivosmoviles.ui.activities.DetailsMarvelItem
import com.example.dispositivosmoviles.ui.activities.dataStore
import com.example.dispositivosmoviles.ui.adapters.MarvelAdapter
import com.example.dispositivosmoviles.ui.data.UserDataStore
import com.example.dispositivosmoviles.ui.utilities.DispositivosMoviles
import com.example.dispositivosmoviles.ui.utilities.Metodos
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var lmanager: LinearLayoutManager
    private lateinit var rvAdapter: MarvelAdapter

    // private var rvAdapter: MarvelAdapter = MarvelAdapter { sendMarvelItem(it) }
    private var limit = 99
    private var offset = 0

    private var marvelCharsItems: MutableList<MarvelChars> = mutableListOf()

    //para hacer en dos columnas
    private lateinit var gManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(layoutInflater, container, false)
        lmanager = LinearLayoutManager(
            requireActivity(), LinearLayoutManager.VERTICAL,
            false
        )
        gManager = GridLayoutManager(requireActivity(), 2)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch(Dispatchers.Main) {
            getDataStore().collect { user ->
                Log.d("------------>> UCE email", user.email)
                Log.d("------------>> UCE name", user.name)
                Log.d("------------>> UCE session", user.session)
            }
        }

        val names = arrayListOf("Karen", "Maria", "Cintia", "Joa", "Pepito de los palotes")

        val adapter1 = ArrayAdapter(
            requireActivity(),
            //el simple spinner no es de nadie :v
            R.layout.simple_layout,
            names
        )

        binding.spinner.adapter = adapter1
        //chargeDataRVAPI(offset = offset, limit = limit)
        chargeDataRVInit(limit, offset)

        binding.rvSwipe.setOnRefreshListener {
            chargeDataRVAPI(offset = offset, limit = limit)
            binding.rvSwipe.isRefreshing = false
            lmanager.scrollToPositionWithOffset(5, 20)
        }

        binding.rvMarvelChars.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(
                        recyclerView,
                        dx,
                        dy
                    )//dy para abajo contando y mostrando, y la dx de izquierda a derecha
                    if (dy > 0) {
                        val v = lmanager.childCount//cuantos elementos tengo
                        val p = lmanager.findFirstVisibleItemPosition()//cual es mi posicion actual
                        val t = lmanager.itemCount//cuantos tengo en toal

                        if ((v + p) >= t) {
                            lifecycleScope.launch((Dispatchers.Main)) {
                                /*  val newItems = JikanAnimeLogic().getAllAnimes()*/
                                val items = with(Dispatchers.IO) {
                                    MarvelLogic().getAllMarvelChars(offset, limit)
                                }

                                rvAdapter.updateListItem(items)
                                this@FirstFragment.offset += limit
                            }
                        }
                    }
                }
            })
        binding.txtFilter.addTextChangedListener { filteredText ->
            val newItems = marvelCharsItems.filter { items ->
                items.name.lowercase().contains(filteredText.toString().lowercase())
            }
            rvAdapter.replaceListAdapter(newItems)
        }
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
                    binding.cardView,
                    "Eliminado de favoritos",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                dao.insertMarvelChar(marvelSavedChar)

                Snackbar.make(
                    binding.cardView,
                    "Agregado a favoritos",
                    Snackbar.LENGTH_LONG
                ).show()

            }
        }

        return true
    }

    fun chargeDataRVAPI(limit: Int, offset: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            marvelCharsItems = withContext(Dispatchers.IO) {
                return@withContext (MarvelLogic().getAllMarvelChars(
                    offset, limit
                ))
            }

            rvAdapter = MarvelAdapter(
                marvelCharsItems,
                fnClick = { sendMarvelItem(it) },
                fnSave = { saveMarvelItem(it) }
            )

            //rvAdapter.items = marvelCharsItems
            binding.rvMarvelChars.apply {
                this.adapter = rvAdapter
                this.layoutManager = gManager
            }
            this@FirstFragment.offset += limit
        }
    }

    fun chargeDataRVInit(limit: Int, offset: Int) {
        if (Metodos().isOnline(requireActivity())) {
            lifecycleScope.launch(Dispatchers.Main) {
                marvelCharsItems = withContext(Dispatchers.IO) {
                    return@withContext MarvelLogic().getInitChars(limit, offset)
                }


                /*   rvAdapter.items = marvelCharsItems
                   binding.rvMarvelChars.apply {
                       this.adapter = rvAdapter
                       this.layoutManager = gManager
                   }*/

                rvAdapter = MarvelAdapter(
                    marvelCharsItems,
                    fnClick = { sendMarvelItem(it) },
                    fnSave = { saveMarvelItem(it) }
                )

                binding.rvMarvelChars.apply {
                    this.adapter = rvAdapter
                    //lo reemplazo por otro manager
                    this.layoutManager = gManager
                }
                this@FirstFragment.offset += limit
            }
        } else {
            //Snackbar.make(requireContext(), "No hay conexion", Snackbar.LENGTH_LONG).show()
            Toast.makeText(requireContext(), "No hay conexion", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getDataStore() =
        requireActivity().dataStore.data.map { prefs ->
            UserDataStore(
                name = prefs[stringPreferencesKey("usuario")].orEmpty(),
                email = prefs[stringPreferencesKey("contrasenia")].orEmpty(),
                session = prefs[stringPreferencesKey("pass")].orEmpty()
            )
        }
}