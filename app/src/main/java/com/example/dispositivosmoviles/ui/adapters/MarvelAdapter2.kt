package com.example.dispositivosmoviles.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.MarvelCharacters2Binding
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.databinding.MarvelCharactersBinding
import com.squareup.picasso.Picasso
class MarvelAdapter2(
    private var items: List<MarvelChars>,
    private var fnClick : (MarvelChars) -> Unit,
    private var fnSave : (MarvelChars) -> Boolean
                    ) :
    RecyclerView.Adapter<MarvelAdapter2.MarvelViewHolder>(){

    //Definir que voy a hacer
    //Trabaja con la vista del metodo de abajo
    class MarvelViewHolder(view: View) : RecyclerView.ViewHolder(view) {




        private val binding : MarvelCharacters2Binding = MarvelCharacters2Binding.bind(view)

        //hacer los cambios en esta funcion
        fun render(item : MarvelChars,
                   fnClick : (MarvelChars) -> Unit,
                   fnSave: (MarvelChars) -> Boolean
        ){
            println("Recibiendo a ${item.name}")
            binding.txtName.text = item.name
            binding.txtComic.text = item.comic
            Picasso.get().load(item.image).into(binding.imgMarvel)

            itemView.setOnClickListener{
                fnClick(item)
//                Snackbar.make(binding.imgMarvel,
//                    item.name,
//                    Snackbar.LENGTH_SHORT)
//                    .show()
            }

            binding.btnSave2.setOnClickListener {
                fnSave(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarvelAdapter2.MarvelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //Crea una vista
        return MarvelViewHolder(
            inflater.inflate(
                R.layout.marvel_characters2,
                parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: MarvelAdapter2.MarvelViewHolder, position: Int) {
        holder.render(items[position], fnClick, fnSave)
    }

    override fun getItemCount(): Int = items.size

    fun updateListItem(newItems: List<MarvelChars>){
        this.items = this.items.plus(newItems)
        notifyDataSetChanged()
    }

    fun replaceListAdapter(newItems: List<MarvelChars>){
        this.items = newItems
        notifyDataSetChanged()
    }




}