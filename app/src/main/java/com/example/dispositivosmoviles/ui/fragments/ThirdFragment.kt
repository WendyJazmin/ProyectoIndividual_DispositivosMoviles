package com.example.dispositivosmoviles.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelFavoriteCharsDB
import com.example.dispositivosmoviles.databinding.FragmentSecondBinding
import com.example.dispositivosmoviles.databinding.FragmentThirdBinding
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.logic.marvelLogic.MarvelLogic
import com.example.dispositivosmoviles.ui.activities.DetailsMarvelItem
import com.example.dispositivosmoviles.ui.adapters.MarvelAdapter
import com.example.dispositivosmoviles.ui.adapters.MarvelAdapter3
import com.example.dispositivosmoviles.ui.utilities.DispositivosMoviles
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ThirdFragment : Fragment() {

    private lateinit var binding: FragmentThirdBinding
    private lateinit var rvAdapter: MarvelAdapter3
    private lateinit var gManager: GridLayoutManager
    private lateinit var lManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentThirdBinding.inflate(
            inflater, container, false
        )

        lManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        //gManager = GridLayoutManager(requireContext(), 2)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        chargeDataRV()


        binding.rvSwipe3.isRefreshing = false

        binding.rvMarvelChars3.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(
                        recyclerView,
                        dx,
                        dy
                    )//dy para abajo contando y mostrando, y la dx de izquierda a derecha
                    if (dy > 0) {
                        val v = lManager.childCount//cuantos elementos tengo
                        val p = lManager.findFirstVisibleItemPosition()//cual es mi posicion actual
                        val t = lManager.itemCount//cuantos tengo en toal

                        if ((v + p) >= t) {
                            lifecycleScope.launch((Dispatchers.Main)) {
                                /*  val newItems = JikanAnimeLogic().getAllAnimes()*/
                                val items = with(Dispatchers.IO) {
                                    // MarvelLogic().getAllMarvelChars(offset, limit)
                                    MarvelLogic().getAllFavoriteMarvelCharDb()
                                }

                                rvAdapter.updateListItem(items)
                                // this@SecondFragment.offset += limit
                            }
                        }
                    }
                }
            })
        /*  binding.txtFilter.addTextChangedListener { filteredText ->
              val newItems = marvelCharsItems.filter { items ->
                  items.name.lowercase().contains(filteredText.toString().lowercase())
              }
              rvAdapter.replaceListAdapter(newItems)
          }*/
    }

    fun sendMarvelItem(item: MarvelChars) {
        val i = Intent(requireContext(), DetailsMarvelItem::class.java)
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
                    binding.cardView3,
                    "Eliminado de favoritos",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                dao.insertMarvelChar(marvelSavedChar)
                Snackbar.make(
                    binding.cardView3,
                    "Agregado a favoritos",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
        return false
    }

    fun chargeDataRV() {
        lifecycleScope.launch(Dispatchers.Main) {
            val favoriteChars = getAllFavoriteMarvelChars()
            rvAdapter = MarvelAdapter3(
                favoriteChars,
                fnClick = { sendMarvelItem(it) },
                fnSave = { saveMarvelItem(it) }
            )
            binding.rvMarvelChars3.apply {
                adapter = rvAdapter
                this.layoutManager = lManager
                // this.layoutManager = gManager//para hacer 2 columnas
            }
        }
    }

    private suspend fun getAllFavoriteMarvelChars(): List<MarvelChars> {
        return withContext(Dispatchers.IO) {
            MarvelLogic().getAllFavoriteMarvelCharDb()
        }
    }
}
