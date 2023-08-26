package com.example.dispositivosmoviles.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.MarvelCharacters3Binding
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.databinding.MarvelCharactersBinding
import com.squareup.picasso.Picasso

class MarvelAdapter3(
    private var items: List<MarvelChars>,
    private var fnClick : (MarvelChars) -> Unit,
    private var fnSave : (MarvelChars) -> Boolean
                    ) :
    RecyclerView.Adapter<MarvelAdapter3.MarvelViewHolder>(){


    class MarvelViewHolder(view: View) : RecyclerView.ViewHolder(view) {



        private val binding : MarvelCharacters3Binding = MarvelCharacters3Binding.bind(view)


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

            binding.btnSave3.setOnClickListener {
                fnSave(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarvelAdapter3.MarvelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //Crea una vista
        return MarvelViewHolder(
            inflater.inflate(
                R.layout.marvel_characters3,
                parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: MarvelAdapter3.MarvelViewHolder, position: Int) {
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